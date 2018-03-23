package com.cdkj.link_community.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.model.MessageDetails;
import com.cdkj.link_community.model.MessageModel;
import com.cdkj.link_community.model.MessageType;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.cdkj.link_community.model.UserInfoModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 李先俊 on 2018/3/22.
 */

public interface MyApiServer {


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


}
