package com.capstone.liveAloneComunity.controller.post;

import com.capstone.liveAloneComunity.dto.post.SearchPostRequestDto;
import com.capstone.liveAloneComunity.repository.post.SearchPostType;
import com.capstone.liveAloneComunity.response.Response;
import com.capstone.liveAloneComunity.service.post.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    @ApiOperation(value = "게시물 단건 조회", notes = "게시물 단건을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getPost(@RequestParam("id") Long id){
        return Response.success(postService.getPost(id));
    }

    @GetMapping("/search")
    @ApiOperation(value = "게시물 검색", notes = "게시물을 검색해 페이징처리해 반환한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response searchPost(@RequestBody SearchPostRequestDto searchPostRequestDto,
                               @PageableDefault Pageable pageable,
                               @RequestParam SearchPostType searchPostType){
        return Response.success(postService.searchPost(searchPostRequestDto, pageable, searchPostType));
    }

    @GetMapping("/")
    @ApiOperation(value = "회원의 게시물 조회", notes = "회원의 게시물을 이름의 역순으로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getMembersPost(@RequestParam("member") Long id, @PageableDefault Pageable pageable){
        return Response.success(postService.getMembersPost(pageable, id));
    }
}
