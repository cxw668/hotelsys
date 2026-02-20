package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Reservation;
import org.chore.hotelsys.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public R<List<Reservation>> list() {
        return R.success("查询成功", reservationService.list());
    }

    @GetMapping("/{id}")
    public R<Reservation> getById(@PathVariable Long id) {
        return R.success("查询成功", reservationService.getById(id));
    }

    @PostMapping
    public R<Reservation> create(@RequestBody Reservation reservation) {
        boolean saved = reservationService.save(reservation);
        return saved ? R.success("创建成功", reservation) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        boolean updated = reservationService.updateById(reservation);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = reservationService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

