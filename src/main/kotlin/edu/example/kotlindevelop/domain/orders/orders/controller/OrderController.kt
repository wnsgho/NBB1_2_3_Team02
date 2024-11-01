package edu.example.kotlindevelop.domain.orders.orders.controller

import edu.example.kotlindevelop.domain.orders.orders.dto.OrderDTO
import edu.example.kotlindevelop.domain.orders.orders.service.OrderService
import edu.example.kotlindevelop.global.security.SecurityUser
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) {


    @PostMapping
    fun createOrder(
        @RequestBody orderDTO: OrderDTO?,
        @AuthenticationPrincipal user: SecurityUser
    ): Map<String, String> {
        val memberId: Long = user.id
        if (orderDTO != null) {
            orderService.createOrder(orderDTO, memberId)
        }

        return java.util.Map.of("success", "create")
    }
    //
    // 주문 조회
    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: Long?): ResponseEntity<OrderDTO> {
        val orderDTO: OrderDTO? = orderService.read(orderId)
        return ResponseEntity.ok(orderDTO)
    }
    //###//
    // 주문 목록 조회
    @GetMapping("/list")
    fun getList(
        @Validated pageRequestDTO: OrderDTO.PageRequestDTO?,
        @AuthenticationPrincipal user: SecurityUser
    ): ResponseEntity<Page<OrderDTO.OrderListDTO>> {
        val memberId: Long = user.id
        return ResponseEntity.ok(pageRequestDTO?.let { orderService.getList(it, memberId) })
    }
    @GetMapping("/liste/{month}")
    fun getListByMonthOrderByOrder(
        @PathVariable month: Int?,
        @Validated pageRequestDTO: OrderDTO.PageRequestDTO?,
        @AuthenticationPrincipal user: SecurityUser
    ): ResponseEntity<Page<OrderDTO.OrderListDTO>> {
        val memberId: Long = user.id
        return ResponseEntity.ok(pageRequestDTO?.let { orderService.getList(month, it, memberId) })
    }
    //
    //
    //
    //
    // 주문 삭제
    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: Long?): Map<String, String> {
        orderService.delete(orderId)
        return java.util.Map.of("success", "delete")
    }

    //주문 월별 그래프 조회
    @GetMapping("/monthly-summary")
    fun getMonthlyOrderSummary(@AuthenticationPrincipal user: SecurityUser): ResponseEntity<List<Map<String, Any?>>> {
        val memberId: Long = user.id
        val monthlySummary: List<Map<String, Any?>> = orderService.getMonthlyOrderSummary(memberId) ?: emptyList()
        return ResponseEntity.ok(monthlySummary)
    }


    @GetMapping("/average-prices")
    fun getMonthlyAveragePrices(): Map<String, Map<String, Int>> {
        return orderService.getMonthlyAveragePrices()
    }

}