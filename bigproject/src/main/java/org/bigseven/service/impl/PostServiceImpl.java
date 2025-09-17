package org.bigseven.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.PostTypeEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.Post;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.PostMapper;
import org.bigseven.mapper.UserMapper;
import org.bigseven.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    @Override
    public void publishPost(Integer userId, Boolean isNicked, Boolean isArgent, PostTypeEnum postType, String title, String content) {
        Post post = Post.builder()
                .userId(userId)
                .isNicked(isNicked)
                .isArgent(isArgent)
                .postType(postType)
                .title(title)
                .content(content)
                .build();
        postMapper.insert(post);
    }

    @Override
    public void markPost(Integer userId,Integer postId,Integer acceptedByUserId, Boolean isAccepted, Boolean isResolved) {
        Post post = postMapper.selectById(postId);
        //一个简单的鉴权
        //警告:这并不是一个安全的鉴权方式，因为它信任了前端传的userId
        //后期应该改为在Controller 或 Service 方法上使用 @PreAuthorize 注解
        User operatorUser = userMapper.selectById(acceptedByUserId);
        if (post != null &&operatorUser != null && operatorUser.getUserType()!= UserTypeEnum.STUDENT) {
           post.setIsAccepted(isAccepted);
           post.setIsResolved(isResolved);
           postMapper.updateById(post);
        }
        else {
            //需要做错误处理
            return;
        }
    }

    @Override
    public void deletePost(Integer userId,Integer postId) {
        Post post = postMapper.selectById(postId);
        if (post != null) {
            if (post.getUserId().equals(userId)) {
                throw new ApiException(ExceptionEnum.INVALID_PARAMETER);
            }
            postMapper.deleteById(postId);
        } else {
            throw new ApiException(ExceptionEnum.RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public void updatePost(Integer postId, PostTypeEnum postType,String title, String content) {
        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setPostType(postType);
            post.setTitle(title);
            post.setContent(content);
            postMapper.updateById(post);
        }
        else {
            //需要做错误处理
            return;
        }

    }
}
