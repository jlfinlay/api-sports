package com.jl.openapi.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jl.db.common.PageBean;
import com.jl.db.common.Response;
import com.jl.db.entity.*;
import com.jl.db.enums.BetStatusEnum;
import com.jl.db.enums.PendingEnum;
import com.jl.db.exception.ServiceException;
import com.jl.db.service.*;
import com.jl.db.utils.bean.BeanUtils;
import com.jl.db.vo.invo.*;
import com.jl.db.vo.outvo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class GameService {


    @Resource
    private TyAccountInfoService tyAccountInfoService;

    @Resource
    private YsbAccountInfoService ysbAccountInfoService;

    @Resource
    private YsbBettingInfoService ysbBettingInfoService;

    @Resource
    private YsbCashoutRecordsService ysbCashoutRecordsService;


    @Resource
    private YsbUserBetDataService ysbUserBetDataService;

    @Resource
    private YsbUserComboDataService ysbUserComboDataService;

    @Resource
    private SportTypeConfigService sportTypeConfigService;

    @Resource
    private BetDataService betDataService;

    @Resource
    private YsbPayoutRecordsService ysbPayoutRecordsService;

    private static final Map<String, String> msgCodeMap = new HashMap<>();
    static {
        //msgCodeMap.put("", "success");
        msgCodeMap.put("", "投注成功");
        //msgCodeMap.put("1001", "Bet payout limit reached");
        msgCodeMap.put("1001", "达到投注限额");
        //msgCodeMap.put("1002", "Duplicate bet. Not allow to place same bet same stake within 5 seconds.");
        msgCodeMap.put("1002", "重复下注。不允许在5秒内下相同注.");
        //msgCodeMap.put("1003", "Event closed for betting");
        msgCodeMap.put("1003", "赛事关闭");
        //msgCodeMap.put("1004", "Insufficient Balance");
        msgCodeMap.put("1004", "余额不足");
        //msgCodeMap.put("1005", "Insufficient Fund");
        msgCodeMap.put("1005", "资金不足");
        //msgCodeMap.put("1006", "Internal error occured");
        msgCodeMap.put("1006", "内部错误");
        //msgCodeMap.put("1007", "Invalid Bet");
        msgCodeMap.put("1007", "无效投注");
        //msgCodeMap.put("1008", "Maximum bet amount reached");
        msgCodeMap.put("1008", "达到最大下注额");
        //msgCodeMap.put("1009", "Minimum bet amount needed");
        msgCodeMap.put("1009", "所需的最低下注额");
        //msgCodeMap.put("1010", "The handicap value has been changed. Please refresh your page to get the latest handicap value");
        msgCodeMap.put("1010", "差点值已更改。请刷新页面以获得最新的差点价值");
        //msgCodeMap.put("1011", "Time exceed 180s");
        msgCodeMap.put("1011", "时间超过180S");
        //msgCodeMap.put("1012", "Warning: Price of the event has been changed");
        msgCodeMap.put("1012", "警告：活动价格已更改");
        //msgCodeMap.put("1013", "Others error");
        msgCodeMap.put("1013", "其他错误");
    }

    public Response saveYsbAccountInfo(YsbAccountInfoVO accountInfoVO) {
        log.info("保存第三方用户注册信息,入参,accountInfoVO:{}", accountInfoVO);
        LambdaQueryWrapper<YsbAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbAccountInfo::getUserId, accountInfoVO.getUserId());
        List<YsbAccountInfo> list = ysbAccountInfoService.list(wrapper);
        if (null == list || list.isEmpty()) {
            BeanUtils.copyProperties(accountInfoVO, YsbAccountInfo::new).insert();
        } else {
            list.stream().forEach(x -> {
                ysbAccountInfoService.removeById(x.getId());
            });
            BeanUtils.copyProperties(accountInfoVO, YsbAccountInfo::new).insert();
        }

        return Response.success();
    }

    public Response<YsbAccountInfoVO> getYsbAccountInfoByUserId(Integer userId) {
        log.info("根据userId获取第三方用户注册信息,入参,userId:{}", userId);
        LambdaQueryWrapper<YsbAccountInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbAccountInfo::getUserId, userId);
        YsbAccountInfo entity = ysbAccountInfoService.getOne(wrapper);
        return Response.successData(BeanUtils.copyProperties(entity, YsbAccountInfoVO::new));
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<GameScoreInfoVO> saveReqBetamtInfo(YsbBettingInfoVO vo) {
        log.info("第三方投注回调数据入库,入参,YsbBettingInfoVO:{}", vo);

        // TransactionID （TRX）是 unique (不重复的)。如果收到相同的 TRX 就别重复扣除餘額
        Long transactionId = vo.getTransactionId();
        Integer userId = vo.getUserId();
        TyAccountInfo userinfo = tyAccountInfoService.getById(userId);
        if (null == userinfo) {
            throw new ServiceException("用户信息不存在");
        }

        LambdaQueryWrapper<YsbBettingInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbBettingInfo::getTransactionId, transactionId);
        int count = ysbBettingInfoService.count(wrapper);
        if (count > 0) {
            //第三方投注回调失败,重复的TransactionID
            throw new ServiceException("投注回调失败!重复的TransactionID");
        }
        // 第三方投注回调数据入库
        BeanUtils.copyProperties(vo, YsbBettingInfo::new).insert();

        // 在收到下注的請求時，請在內部檢查玩家是否有足夠的餘額下注。
        // 如果投注被接受，请务必扣除玩家的餘額。现改为用户投注提交成功后就扣款
        return Response.successData(BeanUtils.copyProperties(tyAccountInfoService.getById(userId), GameScoreInfoVO::new));
    }


    @Transactional(rollbackFor = Exception.class)
    public Response<GameScoreInfoVO> saveBetConfirmationData(YsbBettingInfoVO vo) {
        log.info("第三方投注回调数据入库,入参,YsbBettingInfoVO:{}", vo);

        // TransactionID （TRX）是 unique (不重复的)。
        Long transactionId = vo.getTransactionId();
        Integer userId = vo.getUserId();

        TyAccountInfo tyAccountInfoEntity = tyAccountInfoService.getById(userId);
        if (null == tyAccountInfoEntity) {
            throw new ServiceException("用户信息不存在");
        }

        LambdaQueryWrapper<YsbBettingInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbBettingInfo::getTransactionId, transactionId);
        wrapper.eq(YsbBettingInfo::getUserId, userId);
        YsbBettingInfo entity = ysbBettingInfoService.getOne(wrapper);
        if (entity == null) {
            //第三方投注确认回调失败,流程错误或无效的TransactionID
            throw new ServiceException("第三方投注失败");
        }

        Integer bettingId = entity.getId();

        // 修改投注数据
        YsbBettingInfo updateEntity = BeanUtils.copyProperties(vo, YsbBettingInfo::new);
        updateEntity.setCreateTime(entity.getCreateTime());
        updateEntity.setId(bettingId);
        updateEntity.updateById();

        // 插入投注记录表
        List<YsbBettingRecordsVO> recordsVOS = vo.getBettingRecordsList();
        if (null != recordsVOS && recordsVOS.size() > 0) {
            List<YsbBettingRecords> recordsEntities = BeanUtils.copyProperties(recordsVOS, YsbBettingRecords::new);
            recordsEntities.stream().forEach(x -> {
                x.setBettingId(bettingId).insert();
            });

        }

        // 返回GameScoreInfoVO
        return Response.successData(BeanUtils.copyProperties(tyAccountInfoService.getById(userId), GameScoreInfoVO::new));
    }



    /**
     * @description
     *  派彩 :
     *  派彩金额是正数表示赢
     *  局,
     *  派彩金额是0表示输局
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response<GameScoreInfoVO> saveBetPayOutData(YsbPayoutRecordsVO vo) {
        log.info("第三方投注回调派彩数据入库,入参,YsbPayoutRecordsVO:{}", vo);
        Integer userId = vo.getUserId();
        TyAccountInfo tyAccountInfoEntity = tyAccountInfoService.getById(userId);
        if (null == tyAccountInfoEntity) {
            throw new ServiceException("用户信息不存在");
        }

        // PAYOUT 交易ID（TRX）是唯一的（不重复）。 如果您收到相同的TRX，请不要重复扣除/增加 余额。
        Long transactionId = vo.getTransactionId();
        LambdaQueryWrapper<YsbPayoutRecords> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbPayoutRecords::getTransactionId, transactionId);
        int count = ysbPayoutRecordsService.count(wrapper);
        if (count > 0) {
            throw new ServiceException("派彩失败!重复的TRX:" + transactionId);
        }

        // 判断RefId是否是投注成功的数据
        String referenceId = vo.getReferenceId();
        BetData betData = betDataService.getBetDataByBettingId(referenceId);
        if(BetData.NO_PAY_OUT == betData.getIsPayout()){
            // 未派彩
            Integer betStatus = betData.getBetStatus();
            if(BetData.BET_SUCCESS != betStatus.intValue()){
                throw new ServiceException("派彩失败! 非投注成功状态不允许派彩");
            }
        }

        // 添加派彩金额, 每一次的下注会推送三次派彩,三次推送中,每次推送的TRX是不一样的,REFID是一样
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbPayoutRecords::getReferenceId, referenceId);
        wrapper.eq(YsbPayoutRecords::getUserId, userId);
        wrapper.orderByDesc(YsbPayoutRecords::getId);
        List<YsbPayoutRecords> payoutList = ysbPayoutRecordsService.list(wrapper);
        BigDecimal payoutAmount;
        if (null == payoutList || payoutList.isEmpty()) {
            // 第一次派彩,直接添加余额
            // 获取本次推送金额
            payoutAmount = null == vo.getPayoutAmount() ? BigDecimal.ZERO : vo.getPayoutAmount();
        } else {
            // 获取上一次的推送的金额,用本次推送的金额-上次推送的金额,然后添加到用户余额
            YsbPayoutRecords upEntity = payoutList.get(0);
            BigDecimal upPayoutMant = upEntity.getPayoutAmount();
            if (null == upPayoutMant) {
                upPayoutMant = BigDecimal.ZERO;
            }
            // 获取本次推送金额
            BigDecimal currAmount = null == vo.getPayoutAmount() ? BigDecimal.ZERO : vo.getPayoutAmount();
            payoutAmount = currAmount.subtract(upPayoutMant).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        }

        // 派彩数据入库
        Date payOutDate = new Date();
        vo.setCreateTime(payOutDate);
        BeanUtils.copyProperties(vo, YsbPayoutRecords::new)
                .setAgentId(tyAccountInfoEntity.getOwnerID())
                //.setPayoutAmount(payoutAmount)
                .insert();

        // 修改投注记录表状态(是否派彩),并记录派彩次数
        updateBetDataByPayout(betData,vo);

        // 修改用户钱包
        log.info("第三方投注派彩,操作前用户{}金币：{}", userId, tyAccountInfoEntity.getScore());
        tyAccountInfoService.addUserScore(userId, payoutAmount);
        TyAccountInfo accounts = tyAccountInfoService.getById(userId);
        log.info("第三方投注派彩,操作后用户{}金币：{}", userId, accounts.getScore());

        // 返回GameScoreInfoVO
        return Response.successData(BeanUtils.copyProperties(accounts, GameScoreInfoVO::new));
    }


    private void updateBetDataByPayout(BetData betData,YsbPayoutRecordsVO vo){
        // 修改投注记录表状态(是否派彩),并记录派彩次数
        betData.setIsPayout(BetData.PAY_OUT);
        Integer payoutNum = betData.getPayoutNum();
        betData.setPayoutNum(payoutNum.intValue() + 1);
        // 修改派彩后的状态
        Integer betStatus;
        String msg;
        String payoutStatus = vo.getBetStatus().trim();
        if(YsbPayoutRecords.WIN == Integer.parseInt(payoutStatus)){
            betStatus = BetStatusEnum.WIN.getValue();
            msg = BetStatusEnum.WIN.getDesc();
        }else if(YsbPayoutRecords.LOSE == Integer.parseInt(payoutStatus)){
            betStatus = BetStatusEnum.LOSE.getValue();
            msg = BetStatusEnum.LOSE.getDesc();
        }else if(YsbPayoutRecords.VOID == Integer.parseInt(payoutStatus)){
            betStatus = BetStatusEnum.VOID.getValue();
            msg = BetStatusEnum.VOID.getDesc();
        }else if(YsbPayoutRecords.WIN_HALF == Integer.parseInt(payoutStatus)){
            betStatus = BetStatusEnum.WIN_HALF.getValue();
            msg = BetStatusEnum.WIN_HALF.getDesc();
        }else if(YsbPayoutRecords.LOSE_HALF == Integer.parseInt(payoutStatus)){
            betStatus = BetStatusEnum.LOSE_HALF.getValue();
            msg = BetStatusEnum.LOSE_HALF.getDesc();
        }else {
            betStatus = BetStatusEnum.CANCEL.getValue();
            msg = BetStatusEnum.CANCEL.getDesc();
        }
        betData.setBetStatus(betStatus); //将投注记录的状态更新为派彩后的状态
        betData.setMsg(msg);
        betData.updateById();
    }


    /**
     * @description
     *  获取体育类型配置信息
     * @author finlay
     */
    private SportTypeConfigVO getSportType(BetData betData, TyAccountInfo tyAccountInfoEntity){
        SportTypeConfigVO vo = null;
        List<SportTypeConfigVO> configList = sportTypeConfigService.getSportTypeConfig(tyAccountInfoEntity.getOwnerID(),null);
        String betInfo = betData.getBetInfo();
        log.info("获取体育类型配置信息betInfo:{}",betInfo);
        JSONArray betArray = JSONArray.parseArray(betInfo);
        JSONObject betJSON = (JSONObject) betArray.get(0);
        String sportName = betJSON.getString("sportName");
        log.info("获取体育类型配置信息sportName:{}",sportName);
        if(StringUtils.isNotBlank(sportName)){
            Optional<SportTypeConfigVO> first = configList.stream().filter(x -> sportName.trim().equalsIgnoreCase(x.getGameName())).findFirst();
            if(first.isPresent()){
                vo = first.get();
                log.info("获取体育类型配置信息SportTypeConfigVO:{}",vo);
            }
        }
        return vo;

    }


    public Response saveCashOutData(List<YsbCashoutRecordsVO> list) {
        log.info("结算列表数据入库服务开始,入参,list{}", list);
        List<YsbCashoutRecords> entities = BeanUtils.copyProperties(list, YsbCashoutRecords::new);

        // 入库
        entities.stream().forEach(x -> {
            LambdaQueryWrapper<YsbCashoutRecords> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YsbCashoutRecords::getUserId, x.getUserId()).eq(YsbCashoutRecords::getBetId, x.getBetId());
            YsbCashoutRecords entity = ysbCashoutRecordsService.getOne(wrapper);
            if (null == entity) {
                // 插入
                x.setCreateBy("SYSTEM").setCreateTime(new Date()).insert();
            } else {
                // 修改数据
                x.setId(entity.getId()).setUpdateBy("SYSTEM").setUpdateTime(new Date());
                ysbCashoutRecordsService.updateById(x);
            }
        });
        return Response.success();
    }


    public Response<List<YsbCashoutRecordsVO>> getCashOutRecordsData(CashOutPageVO vo) {
        log.info("获取结算列表数据服务开始,入参,CashOutPageVO{}", vo);
        LambdaQueryWrapper<YsbCashoutRecords> wrapper = new LambdaQueryWrapper<>();
        if (null != vo.getUserId() && vo.getUserId().intValue() != 0) {
            wrapper.eq(YsbCashoutRecords::getUserId, vo.getUserId());
        }

        if (StringUtils.isNotBlank(vo.getStartTime()) && StringUtils.isNotBlank(vo.getEndTime())) {
            wrapper.between(YsbCashoutRecords::getCreateTime, vo.getStartTime(), vo.getEndTime());
        }
        wrapper.orderByDesc(YsbCashoutRecords::getCreateTime);
        return Response.successData(BeanUtils.copyProperties(ysbCashoutRecordsService.list(wrapper), YsbCashoutRecordsVO::new));
    }


    public Response updateCashOutStatus(CashOutUpdateVO updateVO) {
        log.info("修改提前结算状态服务开始,入参, CashOutUpdateVO:{}", updateVO);
        LambdaQueryWrapper<YsbCashoutRecords> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YsbCashoutRecords::getUserId, updateVO.getUserId()).eq(YsbCashoutRecords::getBetId, updateVO.getBetId());
        YsbCashoutRecords entity = ysbCashoutRecordsService.getOne(wrapper);
        if (null != entity && StringUtils.isNotBlank(updateVO.getStatus())) {
            if (Integer.valueOf(updateVO.getStatus()) == 0) {
                entity.setBetStatus("cashout");// 结算成功
            } else {
                entity.setBetStatus("failed"); // 结算失败
            }
            ysbCashoutRecordsService.updateById(entity);
        }
        return Response.success();
    }

    public Response<List<YsbPayoutRecordsVO>> getBetWinOrLoseDate(List<YsbUserBetDataVO> list) {
        log.info("获取输赢金额,入参, YsbUserBetDataVO:{}", list);
        List<YsbPayoutRecordsVO> payoutList = new ArrayList<>();
        // 获取投注id
        list.stream().forEach(x -> {
            YsbPayoutRecordsVO vo = new YsbPayoutRecordsVO();
            String betId = x.getComboId();
            if (betId.equals("0") || StringUtils.isBlank(betId)) {
                betId = x.getBetId();
            }
            Integer userId = x.getUserId();

            // 根据投注id和userId获取派彩记录
            LambdaQueryWrapper<YsbPayoutRecords> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YsbPayoutRecords::getReferenceId, betId);
            wrapper.eq(YsbPayoutRecords::getUserId, userId);
            List<YsbPayoutRecords> entities = ysbPayoutRecordsService.list(wrapper);
            if (null == entities || entities.isEmpty()) {
                // 没有派彩记录
                vo.setUserId(userId);
                vo.setReferenceId(betId);
                vo.setPayoutAmount(BigDecimal.ZERO); //输赢金额
                vo.setBetStatus("0");// 投注成功,未派彩
                payoutList.add(vo);
            } else {
                // 获取输掉比赛的记录(只有一条
                BigDecimal count = BigDecimal.ZERO;
                for (int i = 0; i < entities.size(); i++) {
                    YsbPayoutRecords ysbPayoutRecordsEntity = entities.get(i);
                    if (ysbPayoutRecordsEntity.getBetStatus().equals("2") && ysbPayoutRecordsEntity.getPayoutAmount().compareTo(BigDecimal.ZERO) == 0) {//输
                        vo.setUserId(userId);
                        vo.setReferenceId(betId);
                        vo.setPayoutAmount(x.getBetAmount()); //输赢金额就是用户当前投注额
                        vo.setBetStatus(ysbPayoutRecordsEntity.getBetStatus());// 输
                        payoutList.add(vo);
                        continue;
                    } else {  // 赢
                        count.add(ysbPayoutRecordsEntity.getPayoutAmount());
                        vo.setUserId(userId);
                        vo.setReferenceId(betId);
                        vo.setPayoutAmount(count);
                        vo.setBetStatus("1");//赢
                        payoutList.add(vo);
                    }
                }
            }

        });

        return Response.successData(payoutList);
    }


    public Response saveUserBetData(List<YsbUserBetDataVO> list) {
        log.info("用户投注列表数据入库服务开始,入参, List<YsbUserBetDataVO>:{}", list);
        List<YsbUserBetData> entities = BeanUtils.copyProperties(list, YsbUserBetData::new);

        // 入库
        entities.stream().forEach(x -> {
            LambdaQueryWrapper<YsbUserBetData> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YsbUserBetData::getUserId, x.getUserId()).eq(YsbUserBetData::getBetId, x.getBetId());
            YsbUserBetData entity = ysbUserBetDataService.getOne(wrapper);
            if (null == entity) {
                // 插入
                x.setCreateBy("SYSTEM").setCreateTime(new Date()).insert();
            } else {
                // 修改数据
                x.setId(entity.getId()).setUpdateBy("SYSTEM").setUpdateTime(new Date());
                ysbUserBetDataService.updateById(x);
            }
        });
        return Response.success();
    }

    public Response<List<YsbUserBetDataVO>> getYsbUserBetData(BetDataListVO vo) {
        log.info("用户投注列表数据入库,入参, BetDataListVO:{}", vo);
        LambdaQueryWrapper<YsbUserBetData> wrapper = new LambdaQueryWrapper<>();
        if (null != vo.getUserId() && vo.getUserId().intValue() != 0) {
            wrapper.eq(YsbUserBetData::getUserId, vo.getUserId());
        }

        if (StringUtils.isNotBlank(vo.getStartTime()) && StringUtils.isNotBlank(vo.getEndTime())) {
            wrapper.between(YsbUserBetData::getCreateTime, vo.getStartTime(), vo.getEndTime());
        }
        wrapper.orderByDesc(YsbUserBetData::getCreateTime);
        return Response.successData(BeanUtils.copyProperties(ysbUserBetDataService.list(wrapper), YsbUserBetDataVO::new));
    }

    public Response saveUserComboData(List<YsbUserComboDataVO> list) {
        log.info("保存用户连串过关数据,服务开始,入参, List<YsbUserComboDataVO>:{}", list);
        List<YsbUserComboData> entities = BeanUtils.copyProperties(list, YsbUserComboData::new);

        // 入库
        entities.stream().forEach(x -> {//cid相同·，betid不相同
            LambdaQueryWrapper<YsbUserComboData> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YsbUserComboData::getUserId, x.getUserId())
                    .eq(YsbUserComboData::getBetId, x.getBetId())
                    .eq(YsbUserComboData::getComboId, x.getComboId());
            YsbUserComboData entity = ysbUserComboDataService.getOne(wrapper);
            if (null == entity) {
                // 插入
                x.setCreateBy("SYSTEM").setCreateTime(new Date()).insert();
            } else {
                // 修改数据
                x.setId(entity.getId()).setUpdateBy("SYSTEM").setUpdateTime(new Date());
                ysbUserComboDataService.updateById(x);
            }
        });
        return Response.success();
    }


    public Response<List<YsbUserComboDataVO>> getYsbUserComboData(ComboVO vo) {
        log.info("获取用户连串过关数据,服务开始,入参, ComboVO:{}", vo);
        LambdaQueryWrapper<YsbUserComboData> wrapper = new LambdaQueryWrapper<>();
        if (null != vo.getUserId() && vo.getUserId().intValue() != 0) {
            wrapper.eq(YsbUserComboData::getUserId, vo.getUserId());
        }

        if (StringUtils.isNotBlank(vo.getComboId())) {
            wrapper.eq(YsbUserComboData::getComboId, vo.getComboId());
        }
        wrapper.orderByDesc(YsbUserComboData::getCreateTime);

        return Response.successData(BeanUtils.copyProperties(ysbUserComboDataService.list(wrapper), YsbUserComboDataVO::new));
    }


    public Response editSportGameItemData(SportTypeConfigVO vo) {
        log.info("修改体育游戏列表数据,入参, vo:{}", vo);
        return Response.successData(BeanUtils.copyProperties(vo, SportTypeConfig::new).setUpdateTime(new Date()).updateById());
    }


    /**
     *  业主后台获取投注列表
     */
    public Response getSportBettingData(SportBettingDataPageVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalDetail", BigDecimal.ZERO);
        map.put("totalWinOrLose", BigDecimal.ZERO);
        LambdaQueryWrapper<BetData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BetData::getAgentId,vo.getAgentId());
        if(vo.getGameId() != null){
            TyAccountInfo entity = tyAccountInfoService.getAccountsInfoByGameId(vo.getGameId());
            if(null == entity) {
                map.put("list", new ArrayList<>());
                return Response.successData(map);
            }else{
                wrapper.eq(BetData::getUserId, entity.getUserID());
            }
        }
        if(StringUtils.isNotBlank(vo.getAccounts())){
            TyAccountInfo entity = tyAccountInfoService.getAccountsInfoByAccounts(vo.getAccounts(),vo.getAgentId());
            if(null == entity) {
                map.put("list", new ArrayList<>());
                return Response.successData(map);
            }else{
                wrapper.eq(BetData::getUserId,entity.getUserID());
            }
        }
        if(StringUtils.isNotBlank(vo.getGameCode())){
            wrapper.eq(BetData::getGameCode,vo.getGameCode());
        }
        if(StringUtils.isNotBlank(vo.getLocalOrderNo())){
            wrapper.eq(BetData::getLocalBetNo,vo.getLocalOrderNo());
        }
        // 投注时间
        if(StringUtils.isNotBlank(vo.getStartTime()) && StringUtils.isNotBlank(vo.getEndTime())){
            wrapper.between(BetData::getBetTime,vo.getStartTime(),vo.getEndTime());
        }

        // 投注状态
        String status = vo.getStatus();
        if(StringUtils.isNotBlank(status)){
            wrapper.eq(BetData::getBetStatus,status);
        }
        // 按id倒序
        wrapper.orderByDesc(BetData::getId);

        // 分页查询
        Page<BetData> page = new Page(vo.getPageNo(), vo.getPageSize());
        betDataService.page(page, wrapper);

        PageBean<LocalBetDataVO> pageBean = PageBean.page(page.getCurrent(), page.getSize(), page.getTotal(),
                BeanUtils.copyProperties(page.getRecords(), LocalBetDataVO::new));
        if(page.getTotal() > 0){
            // 计算总投注额
            map.put("totalDetail", page.getRecords().stream().
                    map(BetData::getStake).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,BigDecimal.ROUND_HALF_DOWN));
            // 计算派彩后的总输赢额
            map.put("totalWinOrLose", pageBean.getData().stream().
                    map(LocalBetDataVO::getWinOrloseAmount).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,BigDecimal.ROUND_HALF_DOWN));

            // 计算已派彩后的输赢额
            pageBean.getData().stream().forEach(x -> {
                List<YsbPayoutRecords> payoutList = ysbPayoutRecordsService.selectPayOutData(x.getBettingId());
                if(payoutList != null && payoutList.size()>0){
                    /*if(x.getBetType() == 1){
                        // 获取投注确认数据
                        List<BetSelectionVO> selectionVOS = new ArrayList<>();
                        YsbBettingRecordsEntity record = ysbBettingRecordsService.getBetDateByRefundId(x.getBettingId());
                        if(record != null ){
                            String betInfo = x.getBetInfo();
                            JSONArray array = JSONArray.parseArray(betInfo);
                            Object o = array.get(0);
                            BetSelectionVO betSelectionVO = BeanUtils.copyProperties(o, BetSelectionVO::new);
                            betSelectionVO.setDecimalPrice(record.getOdds());
                            selectionVOS.add(betSelectionVO);
                        }
                        x.setBetInfo(selectionVOS.toString());
                    }*/

                    // 用户展示输赢状态展示最后一次派彩的状态
                    // 用户赢的金额展示最后一次派彩的金额
                    YsbPayoutRecords payOut = payoutList.get(0);
                    BigDecimal winOrLoseAmount = payOut.getPayoutAmount();
                    x.setWinOrloseAmount(winOrLoseAmount);
                    x.setPayOutTime(payOut.getCreateTime()); // 添加派彩时间

                    // 计算系统输赢额
                    String betStatus = payOut.getBetStatus();
                    BigDecimal payOutAmount = payOut.getPayoutAmount();
                    if(YsbPayoutRecords.VOID == Integer.parseInt(betStatus)){
                        //赛事取消 系统输赢就是0 (赛事取消,派彩推送过来的金额是用户的投注金额)
                        x.setSystemAmount(BigDecimal.ZERO);
                    }else if(payOutAmount.compareTo(BigDecimal.ZERO) == 0){
                        //玩家输  系统盈利 = 投注金额
                        x.setSystemAmount(x.getStake());
                    }else if(payOutAmount.compareTo(BigDecimal.ZERO) > 0) {
                        //玩家嬴  系统盈利= -(中奖金额-投注金额)
                        x.setSystemAmount(payOutAmount.subtract(x.getStake()).setScale(2,BigDecimal.ROUND_HALF_DOWN).negate());
                    }
                }
            });
        }
        map.put("list", pageBean);
        return Response.successData(map);
    }

    /**
     * @description
     *  app端获取投注列表
     */
    public Response<Map> getLocalBetList(BetDataListVO vo) {
        LambdaQueryWrapper<BetData> wrapper = new LambdaQueryWrapper<>();
        if(vo.getUserId() != null){
            wrapper.eq(BetData::getUserId,vo.getUserId());
        }
        if(StringUtils.isNotBlank(vo.getGameCode())){
            wrapper.eq(BetData::getGameCode,vo.getGameCode());
        }

        // 投注时间
        if(StringUtils.isNotBlank(vo.getStartTime()) && StringUtils.isNotBlank(vo.getEndTime())){
            wrapper.between(BetData::getBetTime,vo.getStartTime(),vo.getEndTime());
        }

        // 派彩或未派彩
        String status = vo.getStatus();
        if(StringUtils.isNotBlank(status)){
            if(PendingEnum.PENDING.getCode().equalsIgnoreCase(status)){ // 未派彩
                wrapper.eq(BetData::getIsPayout, PendingEnum.PENDING.getValue());
            }else if(PendingEnum.PROCES.getCode().equalsIgnoreCase(status)){
                wrapper.eq(BetData::getIsPayout,PendingEnum.PROCES.getValue()); // 已派彩
            }
        }

        // 按id倒序
        wrapper.orderByDesc(BetData::getId);

        // 分页查询
        Page<BetData> page = new Page(vo.getPageIndex(), vo.getPageSize());
        return getLocalBetList(page,wrapper,status);
    }

    /**
     * @description
     *  返回统一处理的投注列表数据
     */
    private Response<Map> getLocalBetList(Page<BetData> page,LambdaQueryWrapper<BetData> wrapper,String status){
        Map<String, Object> map = new HashMap<>();
        map.put("totalDetail", BigDecimal.ZERO);
        map.put("totalWinOrLose", BigDecimal.ZERO);

        betDataService.page(page, wrapper);
        PageBean<LocalBetDataVO> pageBean = PageBean.page(page.getCurrent(), page.getSize(), page.getTotal(),
                BeanUtils.copyProperties(page.getRecords(), LocalBetDataVO::new));
        if(page.getTotal() > 0){
            // 计算总投注额
            List<BetData> list = betDataService.list(wrapper);
            BigDecimal totalDetail = list.stream().map(BetData::getStake).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            map.put("totalDetail", totalDetail);

            if(PendingEnum.PROCES.getCode().equalsIgnoreCase(status)){ // 已派彩
                // 计算单笔派彩后的输赢额
                pageBean.getData().stream().forEach(x -> {
                    String bettingId = x.getBettingId();
                    List<YsbPayoutRecords> payoutList = ysbPayoutRecordsService.selectPayOutData(bettingId);
                    if(payoutList != null && payoutList.size()>0){
                        // 用户展示输赢状态展示最后一次派彩的状态
                        // 用户赢的金额展示最后一次派彩的金额
                        BigDecimal winOrLoseAmount = payoutList.get(0).getPayoutAmount();
                        // 输赢金额需要减去投注金额
                        x.setWinOrloseAmount(winOrLoseAmount.subtract(x.getStake()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    }
                });

                // 计算派彩后的总输赢额
                List<LocalBetDataVO> localBetDataVOS =  BeanUtils.copyProperties(list, LocalBetDataVO::new);
                localBetDataVOS.stream().forEach(m -> {
                    String bettingId = m.getBettingId();
                    List<YsbPayoutRecords> payoutList = ysbPayoutRecordsService.selectPayOutData(bettingId);
                    if(payoutList != null && payoutList.size()>0){
                        // 用户展示输赢状态展示最后一次派彩的状态
                        // 用户赢的金额展示最后一次派彩的金额
                        BigDecimal winOrLoseAmount = payoutList.get(0).getPayoutAmount();
                        m.setWinOrloseAmount(winOrLoseAmount);
                    }
                });
                BigDecimal totalWinOrLose = localBetDataVOS.stream().
                        map(LocalBetDataVO::getWinOrloseAmount).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,BigDecimal.ROUND_HALF_DOWN);
                map.put("totalWinOrLose", totalWinOrLose.subtract(totalDetail).setScale(2,BigDecimal.ROUND_HALF_DOWN));

            }
        }
        map.put("list", pageBean);
        return Response.successData(map);
    }


    public Response<List<SportTypeConfigVO>> getSportTypeConfig(Integer agentId) {
        return Response.successData(sportTypeConfigService.getSportTypeConfig(agentId,0));
    }


    @Transactional(rollbackFor = Exception.class)
    public Response saveBetDataRefNo(BetSubmitRefNoVO vo) {
        log.info("保存投注信息,BetSubmitRefNoVO:{}",vo);
        TyAccountInfo account = tyAccountInfoService.getById(vo.getUserId());
        // 投注提交成功,扣除用户投注金额
        deductBalance(vo);
        // 数据入库
        vo.getBetStakeList().stream().forEach(x->{
            BetData entity =  new BetData();
            entity.setUserId(vo.getUserId());
            entity.setGameId(account.getUserID());
            entity.setAgentId(account.getOwnerID());
            entity.setSubmitType(x.getSubmitType());
            entity.setStake(x.getStake());
            entity.setLocalBetNo(x.getLocalBetNo());
            entity.setBetType(x.getSubmitType().intValue()>1 ? 2:x.getSubmitType());//投注类型
            entity.setGameCode(vo.getGameCode());
            entity.setOddType(vo.getOddType());
            entity.setBetModel(vo.getBetModel());
            entity.setOddModel(vo.getOddModel());
            entity.setBetStatus(x.getBetStatus());
            entity.setMsg(x.getErrMsg());
            entity.setBetInfo(JSONObject.toJSONString(x.getBetSelectionList()));
            entity.setBetTime(x.getBetTime());
            entity.setBetBy(account.getAccounts());
            entity.setCreateTime(new Date());
            entity.insert();
            // 保存投注选项明细
            List<BetSelectionVO> betSelectionList = x.getBetSelectionList();
            betSelectionList.stream().forEach(m->{
                BeanUtils.copyProperties(m,BetDataSelection::new)
                        .setCreateTime(entity.getCreateTime())
                        .setBetDataId(String.valueOf(entity.getId())).insert();
            });

        });
        return Response.success();
    }

    /**
     * @description
     *  投注提交成功,扣除用户投注金额
     * @date Created in 2020/10/3 13:06
     * @return
     */
    private void deductBalance(BetSubmitRefNoVO vo){
        //投注提交成功,扣除用户投注金额
        Integer userId = vo.getUserId();
        TyAccountInfo accounts = tyAccountInfoService.getById(userId);

        // 扣除用户余额,修改用户钱包
        log.info("第三方投注回调,操作前用户{}金币：{}", userId, accounts.getScore());
        tyAccountInfoService.subtractUserScore(userId, vo.getTotalBetAmount());
        log.info("第三方投注回调,操作后用户{}金币：{}", userId, tyAccountInfoService.getById(userId).getScore());
    }



    public Response<List<SportTypeConfigVO>> getSportTypeConfigList(Integer agentId) {
        return Response.successData(sportTypeConfigService.getSportTypeConfig(agentId,null));
    }

    public Response getUserInfo(Integer userId) {
        return Response.successData(tyAccountInfoService.getById(userId));
    }

    public Response<GameScoreInfoVO> getUserGameScoreInfo(Integer userId) {
        return Response.successData(BeanUtils.copyProperties(tyAccountInfoService.getById(userId),GameScoreInfoVO::new));
    }


    /**
     * @description
     *  投注状态确认信息入库
     */
    @Transactional(rollbackFor = Exception.class)
    public Response saveBetStatusData(JSONObject json) {
        // 投注订单号
        String refNo = json.getString("RN");
        if(StringUtils.isBlank(refNo)){
            throw new ServiceException("投注状态确认失败!refNo不能为空");
        }

        // 获取本地投注信息
        BetData entity = betDataService.getBetDataByRefNo(refNo);
        if(null == entity){
            throw new ServiceException("投注状态确认失败!refNo订单信息不存在");
        }
        if(entity.getBetStatus().intValue() >= BetData.BET_SUCCESS ){
            throw new ServiceException("投注状态确认失败!该订单已确认,不允许重复确认!");
        }

        //获取投注数据
        Integer betStatus =json.getInteger("STATUS"); // 投注状态
        String bettingId = json.getString("BID"); // 第三方投注单号
        Integer betType = json.getInteger("TT"); // 投注类型
        String errCode = json.getString("ERR_CODE"); // 错误信息
        if(betStatus == null || betStatus.intValue() < BetData.BET_SUCCESS || betStatus.intValue() >BetData.BET_FAIL){
            throw new ServiceException("投注状态确认失败!无法识别的投注状态!");
        }

        // 投注成功,bettingId为必传
        if(BetData.BET_SUCCESS == betStatus.intValue()) {
            if (StringUtils.isBlank(bettingId)) {
                throw new ServiceException("投注状态确认失败!BID不能为空");
            }
        }

        // 如果投注失败,需要给用户钱包加上之前投注扣掉的金额
        if(BetData.BET_FAIL == betStatus.intValue()){
            // 获取userId 前缀_userId
            String userName = json.getString("UN");
            String userId = userName.split("_")[1];
            TyAccountInfo accounts = tyAccountInfoService.getById(userId);
            log.info("第三方投注回调状态确认,投注失败,添加用户金币前用户{}金币：{}", userId, accounts.getScore());
            tyAccountInfoService.addUserScore(Integer.parseInt(userId), entity.getStake());
            log.info("第三方投注回调状态确认,投注失败,添加用户金币后用户{}金币：{}", userId, tyAccountInfoService.getById(Integer.parseInt(userId)).getScore());
        }


        // 修改用户投注信息
        entity.setBettingId(bettingId);
        entity.setBetStatus(betStatus);
        entity.setBetType(betType);
        entity.setMsg(msgCodeMap.get(errCode));
        entity.setUpdateTime(new Date());
        entity.updateById();
        return Response.success();
    }



}
