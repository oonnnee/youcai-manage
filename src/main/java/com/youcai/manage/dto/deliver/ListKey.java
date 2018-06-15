package com.youcai.manage.dto.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListKey {
    private Integer driverId;
    private String guestId;
    private String driverName;
    private String guestName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListKey listKey = (ListKey) o;
        return Objects.equals(driverId, listKey.driverId) &&
                Objects.equals(guestId, listKey.guestId) &&
                Objects.equals(driverName, listKey.driverName) &&
                Objects.equals(guestName, listKey.guestName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(driverId, guestId, driverName, guestName);
    }

    //    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof ListKey)){
//            return false;
//        }
//        if (this == obj){
//            return true;
//        }
//        ListKey listKey = (ListKey)obj;
//        if (listKey.driverId.equals(this.driverId) &&
//                listKey.guestId.equals(this.guestId) &&
//                listKey.getDriverName().equals(this.driverName) &&
//                listKey.getGuestName().equals(this.guestName)){
//            return true;
//        }
//        return false;
//    }
}
