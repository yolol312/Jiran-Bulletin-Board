package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Vo.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.Vo.Category.CategoryRepository;
import com.example.jiranbulletinboard.Domain.File.FileEntity;
import com.example.jiranbulletinboard.Domain.User.UserEntity;
import com.example.jiranbulletinboard.Domain.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /*public Page<PostDTO> findPost(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntities = postRepository.findAll(pageable);
        return postEntities.map(PostEntity::toDTO);
    }
     */

    public Page<PostDetails> findPost(Integer page, Integer size, Integer categoryId, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        // categoryId와 keyword가 있는 경우, 둘 다 필터링
        if (categoryId != null && keyword != null && !keyword.isEmpty()) {
            return postRepository.findPostsWithDetails(pageable, categoryId, keyword);
        }
        // categoryId가 있을 경우, 카테고리만 필터링
        else if (categoryId != null) {
            return postRepository.findPostsByCategory(pageable, categoryId);
        }
        // keyword가 있을 경우, 제목으로만 필터링
        else if (keyword != null && !keyword.isEmpty()) {
            return postRepository.findPostsByKeyword(pageable, keyword);
        }

        // 기본적으로 모든 게시글 조회
        return postRepository.findPostsWithDetails(pageable, null, null);
    }

    /* 나중에 시간 되면 여러 단어로도 검색이 가능하게 만들기
    public List<ChargerDTO> chargerSelectByAddress(String address){
        Set<ChargerEntity> chargers = new HashSet<>();
        List<ChargerDTO> chargersDTO = new ArrayList<>();
        String[] addressList = address.split(" ");
        try{
            for (int i = 0; i<addressList.length; i++){
                if(i==0){
                    chargers.addAll(this.chargerRepository.findChargersByAddress(addressList[i]));
                }else{
                    chargers.retainAll(this.chargerRepository.findChargersByAddress(addressList[i]));
                }
            }
            // chargers 리스트가 비어 있는지 체크하고, 비어 있지 않다면 내용을 출력
            if (chargers.isEmpty()) {
                System.out.println("충전소 데이터가 없습니다.");
                throw new EntityNotFoundException();
            } else {
                for(ChargerEntity chargerEntitys : chargers) {
                    chargersDTO.add(chargerEntitys.toDTO());
                }
                return chargersDTO;
            }
        }catch (Exception exception){
            throw new EntityNotFoundException();
        }
    }
    */
    public void createPost(PostDTO postDTO, MultipartFile[] multipartFiles) {
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryId(postDTO.getCategoryId()).build();
        UserEntity userEntity = UserEntity.builder().userId(postDTO.getUserId()).build();

        PostEntity postEntity = PostEntity.builder()
                .title(postDTO.getTitle())
                .category(categoryEntity)
                .content(postDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isBulletin(postDTO.getIsBulletin())
                .user(userEntity).build();

        PostEntity savedPostEntity = postRepository.save(postEntity);

        List<FileEntity> fileEntities = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            if (!file.isEmpty()) {
                try {
                    // 예: 로컬 디렉토리에 저장
                    String uploadDir = "C:/uploads/" + postDTO.getUserId() + "/" + savedPostEntity.getPostId();
                    // 디렉토리 경로 생성
                    File directory = new File(uploadDir);
                    // 디렉토리가 존재하지 않으면 생성
                    if (!directory.exists()) {
                        directory.mkdirs(); // 상위 디렉토리 포함해서 생성
                    }
                    String filePath = uploadDir + "/" + file.getOriginalFilename();

                    file.transferTo(new File(filePath));

                    FileEntity fileEntity = FileEntity.builder()
                            .fileName(file.getOriginalFilename())
                            .filePath(filePath)
                            .post(postEntity)
                            .build();

                    fileEntities.add(fileEntity);
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류 발생", e);
                }
            }
        }
        postEntity.setFiles(fileEntities);
        postRepository.save(postEntity);
    }

    public PostDTO selectPost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        return postEntity.toDTO();
    }

    public PostDetails selectPostDetail(Integer id) {
        return postRepository.findPostDetailsByPostId(id);
    }


    public PostDTO updatePost(Integer id, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryId(postDTO.getCategoryId()).build();
        List<FileEntity> fileEntity = new ArrayList<>();
        for (Integer file : postDTO.getFiles()) {
            fileEntity.add(FileEntity.builder().fileId(file).build());
        }

        postEntity.setTitle(postDTO.getTitle());
        postEntity.setCategory(categoryEntity);
        postEntity.setContent(postDTO.getContent());
        postEntity.setIsBulletin(postDTO.getIsBulletin());
        postEntity.setFiles(fileEntity);
        PostEntity updatedPost = postRepository.save(postEntity);
        return updatedPost.toDTO();
    }

    public void deletePost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(postEntity);
    }
}