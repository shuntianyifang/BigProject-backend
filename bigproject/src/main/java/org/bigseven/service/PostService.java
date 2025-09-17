package org.bigseven.service;

import org.bigseven.constant.PostTypeEnum;

public interface PostService {

    public void publishPost(Integer userId, Boolean isNicked, Boolean isArgent, PostTypeEnum postType, String title, String content);
    //需要鉴权
    public void markPost(Integer userId,Integer postId, Integer acceptedByUserID,Boolean isAccepted, Boolean isResolved);
    //需要鉴权
    public void deletePost(Integer userId,Integer postId);

    public void updatePost(Integer postId, PostTypeEnum postType,String title, String content);
}
