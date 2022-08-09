package com.ssafy.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.web.db.repository.TheraRepository;
import com.ssafy.web.model.response.RecommendTherapistMapping;
import com.ssafy.web.model.response.RecommendTherapistResponse;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{
	private final TheraRepository threpo;
	
	@Override
	public List<RecommendTherapistResponse> findByUser_UserIdIn(String... user_id) {
//		List<RecommendTherapistResponse> list = threpo.findByUser_UserIdIn(user_id);
		return null;
	}

}
