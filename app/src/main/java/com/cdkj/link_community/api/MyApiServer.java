package com.cdkj.link_community.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.model.ActiveUserModel;
import com.cdkj.link_community.model.AddMarketModel;
import com.cdkj.link_community.model.BannerModel;
import com.cdkj.link_community.model.CoinBBSDetails;
import com.cdkj.link_community.model.CoinBBSHotCircular;
import com.cdkj.link_community.model.CoinBBSInfoTotalCount;
import com.cdkj.link_community.model.CoinBBSListModel;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.CoinPlatformType;
import com.cdkj.link_community.model.CoinPrice;
import com.cdkj.link_community.model.CollectionList;
import com.cdkj.link_community.model.CommentResult;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.model.MarketCoinRecommendTab;
import com.cdkj.link_community.model.MarketCoinTab;
import com.cdkj.link_community.model.MessageDetails;
import com.cdkj.link_community.model.MessageModel;
import com.cdkj.link_community.model.MessageType;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.cdkj.link_community.model.MyActiveModel;
import com.cdkj.link_community.model.MyChooseMarket;
import com.cdkj.link_community.model.UserBBSComment;
import com.cdkj.link_community.model.UserInfoModel;
import com.cdkj.link_community.model.UserMessageComment;
import com.cdkj.link_community.model.UserWarnModel;
import com.cdkj.link_community.model.VersionModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 李先俊 on 2018/3/22.
 */

public interface MyApiServer {

    /**
     * 获取轮播图
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<BannerModel>> getBanner(@Field("code") String code, @Field("json") String json);

    /**
     * 获取版本
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<VersionModel>> getVersion(@Field("code") String code, @Field("json") String json);


    /**
     * 获取币吧详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinBBSDetails>> getCoinBBsDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取币吧统计信息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinBBSInfoTotalCount>> getCoinBBsTotalCountDetails(@Field("code") String code, @Field("json") String json);


    /**
     * 获取添加列表类型
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<AddMarketModel>> getAddMarketList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取我的自选列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MyChooseMarket>>> getMyChooseMarketList(@Field("code") String code, @Field("json") String json);

    /**
     * 获币吧列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<CoinBBSListModel>>> getCoinBBSList(@Field("code") String code, @Field("json") String json);

    /**
     * 获币圈圈子列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<CoinBBSHotCircular>>> getCoinBBSCircularList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取币列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<CoinListModel>>> getCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取币价格列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<CoinPrice>>> getCoinPriceList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取我的资讯评论
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<UserMessageComment>>> getUserMyCommentList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取我的币圈评论
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<UserBBSComment>>> getUserBBSCommentList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取收藏列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<CollectionList>>> getCollectionList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取币种类型列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<CoinPlatformType>> getCoinTypeList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取资讯详情最新评论列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MsgDetailsComment>>> getMsgDetailsNewCommentList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取资讯详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MessageDetails>> getMessageDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取资讯评论详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MsgDetailsComment>> getMessageCommentDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取帖子评论详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinBBSHotCircular>> getBBSCommentDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户信息详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取快讯列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<FastMessage>>> getFastMsgList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取快讯列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<FastMessage>> getFastMsg(@Field("code") String code, @Field("json") String json);

    /**
     * 获取资讯类型
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MessageType>> getMessageType(@Field("code") String code, @Field("json") String json);

    /**
     * 获取资讯列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MessageModel>>> getMsgList(@Field("code") String code, @Field("json") String json);

    /**
     * 获币吧列表
     *
     * @param code
     * @param json
     * @return
             */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<ActiveModel>>> getActiveList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取活动用户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<ActiveUserModel>>> getActiveUser(@Field("code") String code, @Field("json") String json);

    /**
     * 获币吧列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MyActiveModel>>> getMyActiveList(@Field("code") String code, @Field("json") String json);

    /**
     * 获币吧列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ActiveModel>> getActive(@Field("code") String code, @Field("json") String json);

    /**
     * 行情详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinListModel>> getMarket(@Field("code") String code, @Field("json") String json);

    /**
     * 获取资讯列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<UserWarnModel>>> getUserWarnList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取币价格列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinTab>> getMarketCoinTab(@Field("code") String code, @Field("json") String json);

    /**
     * 获取币价格列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinRecommendTab>> getMarketCoinRecommendTab(@Field("code") String code, @Field("json") String json);

    /**
     * 评论结果详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CommentResult>> getCommentResult(@Field("code") String code, @Field("json") String json);
}
