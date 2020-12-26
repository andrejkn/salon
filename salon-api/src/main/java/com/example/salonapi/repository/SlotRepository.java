package com.example.salonapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.salonapi.Slot;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotRepository extends CrudRepository<Slot, Long> {
    @Query(value = "select slot.id, selected_service_id, stylist_name, slot_for, status, locked_at, confirmed_at from slot\n" +
            "left outer join slot_available_services sas on slot.id = sas.slot_id\n" +
            "left outer join salon_service_detail ssd on sas.available_services_id = ssd.id\n" +
            "where slot.status = 0\n" +
            "  and sas.available_services_id = :serviceId\n" +
            "  and now() < slot_for" +
            "  and date_trunc('day', slot.slot_for) = to_date(:formattedDate, 'YYYY-MM-DD')\n" +
            "order by slot_for",
            nativeQuery = true)
    List<Slot> findAvailableSlotsByServiceIdAndSlotFor(@Param("serviceId") Long serviceId,
                                                       @Param("formattedDate") String formattedDate);
}
