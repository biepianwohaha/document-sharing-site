package com.jiaruiblog.controller;

import com.jiaruiblog.auth.Permission;
import com.jiaruiblog.auth.PermissionEnum;
import com.jiaruiblog.entity.dto.BasePageDTO;
import com.jiaruiblog.entity.Comment;
import com.jiaruiblog.entity.dto.CommentDTO;
import com.jiaruiblog.entity.dto.CommentListDTO;
import com.jiaruiblog.service.ICommentService;
import com.jiaruiblog.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @ClassName CommentController
 * @Description 评论系统的控制器
 * @Author luojiarui
 * @Date 2022/6/4 3:11 下午
 * @Version 1.0
 **/
@Api(tags = "评论模块")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @ApiOperation(value = "2.6 查询评论列表", notes = "更新评论")
    @GetMapping("queryDocReviewList")
    public BaseApiResult queryDocReviewList(@ModelAttribute("pageParams") BasePageDTO pageParams) {
        return BaseApiResult.success();
    }

    @ApiOperation(value = "2.5 新增单个评论", notes = "新增单个评论")
    @PostMapping(value = "/auth/insert")
    public BaseApiResult insert(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        return commentService.insert(getComment(commentDTO, request));
    }

    @ApiOperation(value = "2.6 更新评论", notes = "更新评论")
    @PostMapping(value = "/auth/update")
    public BaseApiResult update(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        return commentService.update(getComment(commentDTO, request));
    }

    @ApiOperation(value = "2.7 根据id移除某个评论", notes = "根据id移除某个评论")
    @DeleteMapping(value = "/auth/remove")
    public BaseApiResult remove(@RequestBody Comment comment, HttpServletRequest request) {
        String userId = (String) request.getAttribute("id");
        return commentService.remove(comment, userId);
    }

    @ApiOperation(value = "2.8 根据文档id查询相关评论", notes = "根据id查询某个评论")
    @PostMapping(value = "/list")
    public BaseApiResult queryById(@RequestBody CommentListDTO comment) {
        return commentService.queryById(comment);
    }

    /**
     * @return com.jiaruiblog.entity.Comment
     * @Author luojiarui
     * @Description // 类型转换
     * @Date 10:18 下午 2022/6/23
     * @Param [commentDTO, request]
     **/
    private Comment getComment(CommentDTO commentDTO, HttpServletRequest request) {
        commentDTO = Optional.ofNullable(commentDTO).orElse(new CommentDTO());
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setDocId(commentDTO.getDocId());
        comment.setUserName((String) request.getAttribute("username"));
        comment.setUserId((String) request.getAttribute("id"));
        return comment;
    }

    /**
     * @Author luojiarui
     * @Description 查询全部的用户评论列表
     * TODO 这里有漏洞，管理员也是有自己的个人空间的！
     * @Date 14:38 2022/12/10
     * @Param [pageDTO, request]
     * @return com.jiaruiblog.util.BaseApiResult
     **/
    @Permission({PermissionEnum.ADMIN, PermissionEnum.USER})
    @ApiOperation(value = "查询全部的用户评论", notes = "只有管理员有权限进行所有评论的分类查询")
    @PostMapping(value = "/auth/listALL")
    public BaseApiResult queryAllComments(@RequestBody BasePageDTO pageDTO, HttpServletRequest request) {
        String userId = (String) request.getAttribute("id");
        return commentService.queryAllComments(pageDTO, userId);
    }
}
