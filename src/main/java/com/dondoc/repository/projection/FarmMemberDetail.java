package com.dondoc.repository.projection;

import java.time.LocalDateTime;

public interface FarmMemberDetail {
      Long getUserId();
      String getName();
      Integer getCurrentPigLevel();
      Integer getCurrentHouseLevel();
      LocalDateTime getJoinedAt();
}
