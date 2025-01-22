package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.User.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class PostSpecification {

    public static Specification<PostEntity> withChargerIds(List<UserEntity> chargerIdList) {
        return (root, query, criteriaBuilder) -> {
            if (chargerIdList == null || chargerIdList.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return root.join("chargerId").in(chargerIdList);
        };
    }

    public static Specification<PostEntity> withChargerId(Integer chargerId) {
        return (root, query, criteriaBuilder) -> {
            if (chargerId == null) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.equal(root.get("chargerId").get("id"), chargerId);
        };
    }

    public static Specification<PostEntity> withDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
        };
    }

    public static Specification<PostEntity> combineConditions(List<UserEntity> chargerIdList, Integer chargerId,
                                                                LocalDateTime startDate, LocalDateTime endDate) {
        Specification<PostEntity> spec = Specification.where(null);

        if (chargerIdList != null && !chargerIdList.isEmpty()) {
            spec = spec.and(withChargerIds(chargerIdList));
        }

        if (chargerId != null) {
            spec = spec.and(withChargerId(chargerId));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(withDateRange(startDate, endDate));
        }

        return spec;
    }
}
