package com.youcai.manage.vo.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
public class ListVO {

    private String guestId;
    private String guestName;
    private Integer driverId;
    private String driverName;
    private Date date;

    public ListVO(ListVO listVO){
        this.guestId = listVO.guestId;
        this.guestName = listVO.guestName;
        this.driverId = listVO.driverId;
        this.driverName = listVO.driverName;
        this.date = listVO.date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListVO listVO = (ListVO) o;
        return Objects.equals(guestId, listVO.guestId) &&
                Objects.equals(guestName, listVO.guestName) &&
                Objects.equals(driverId, listVO.driverId) &&
                Objects.equals(driverName, listVO.driverName) &&
                Objects.equals(date, listVO.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(guestId, guestName, driverId, driverName, date);
    }
}
