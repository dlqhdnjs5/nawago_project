package com.study.mk1.cmp.components;

import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;

public interface ShowOffComponent {
	void addShowOff(ShowOffInfoDTO dto);

	long addShowOffReply(ShowOffInfoDTO dto);

	long updateShowOffLike(ShowOffLikeJpa dto);

	void deleteShowOff(ShowOffInfoDTO dto);
}
