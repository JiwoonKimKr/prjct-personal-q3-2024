package com.givemetreat.invoice;

import java.util.*;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.api.utils.PrivatePortOneApi;
import com.givemetreat.invoice.bo.InvoiceBO;
import com.givemetreat.invoice.domain.InvoiceEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Invoice RestController", description = "[Client] Invoice RestAPI Controller; 주문(인보이스) 관련 RestAPI 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/invoice")
@RestController
public class InvoiceRestController {
	private final InvoiceBO invoiceBO;
	private final PrivatePortOneApi privatePortOneApi;

	//포트원 결제 적용으로 수정_21 08 2024
	@Operation(summary = "payment() 해당 주문 결제 시도", description = " 해당 주문 결제 시도")
	@Parameters({
		@Parameter(name = "<String> listItemsOrdered", description = "Json String('Json Array') 형태로 넘어온 주문 상품 리스트 ")
		, @Parameter(name = "<Integer> payment", description = "결제 금액, 원 단위", example = "25000")
		, @Parameter(name = "<String> buyerName", description = "구매자 성명", example = "유재석")
		, @Parameter(name = "<String> buyerPhoneNumber", description = "구매자 연락처(휴대폰)", example = "01012345678")
		, @Parameter(name = "<String> receiverName", description = "수령자 성명", example = "조세호")
		, @Parameter(name = "<String> receiverPhoneNumber", description = "수령자 연락처(휴대폰)", example = "01087654321")
		, @Parameter(name = "<String> address", description = "배송 주소", example = "서울시 서초구 강남구 역삼동")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 이용 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"주문 송장(Invoice) 생성 시도가 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"주문 송장(Invoice) 생성 시도가 성공하였습니다.\"" 
																+ "<br>, \"storeId\" &lt;Integer&gt; storeId "
																+ "<br>, \"channelKey\" channelKey"
																+ "<br>, \"paymentId\" givemetreat-'${invoice.id}' "
																+ "<br>, \"orderName\" givemetreat-'${invoice.id}'-'${invoice.createdAt}' "
																+ "<br>, \"totalAmount\" payment"
																+ "<br>, \"fullName\" buyerName "
																+ "<br>, \"email\" session.loginId "
																+ "<br>, \"phoneNumber\" buyerPhoneNumber "
																+ "<br>, \"invoiceId\" &lt;Integer&gt; invoice.id "
																+ "<br>, \"addressLine1\" 구매자 일반주소 "
																+ "<br>, \"addressLine2\" 구매자 상세주소 "
		
		, content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/payment-validation")
	public Map<String, Object> payment(@RequestParam String listItemsOrdered
										, @RequestParam Integer payment
										, @RequestParam String buyerName
										, @RequestParam String buyerPhoneNumber
										, @RequestParam String receiverName
										, @RequestParam String receiverPhoneNumber
										, @RequestParam String address
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();

		log.info("[InvoiceRestController payment()]"
		+ " @RequestParam jsonString:{}"
		+ ", payment:{}"
		+ ", buyerName:{}"
		+ ", buyerPhoneNumber:{}"
		+ ", receiverName:{}"
		+ ", receiverPhoneNumber:{}"
		+ ", address:{}"
		, listItemsOrdered
		, payment, buyerName, buyerPhoneNumber, receiverName, receiverPhoneNumber, address);
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[InvoiceRestController payment()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		
		InvoiceEntity invoice = invoiceBO.generateInvoiceFromJsonString(listItemsOrdered
																				, userId
																				, payment
																				, buyerName
																				, buyerPhoneNumber
																				, receiverName
																				, receiverPhoneNumber
																				, address);
		
		//결제 요청 실패 시
		if(ObjectUtils.isEmpty(invoice) == true) {
			log.info("[InvoiceRestController payment()] current invoice got failed to get paid.");
			result.put("code", 500);
			result.put("error_message", "주문 송장(Invoice) 생성 시도가 실패하였습니다.");
			return result;			
		}
		
		log.info("[InvoiceRestController payment()] current invoice has success to get paid.");
		result.put("code", 200);
		result.put("result", "주문 송장(Invoice) 생성 시도가 성공하였습니다.");
		result.put("storeId", privatePortOneApi.storeId);
		result.put("channelKey", privatePortOneApi.channelKey);
		//★★★★★paymentId; givemetreat-{invoice.id}
		result.put("paymentId", String.format("givemetreat-%s",invoice.getId()));
		result.put("orderName", String.format("orderName-%s-%s", invoice.getId(), invoice.getCreatedAt()));
		result.put("totalAmount", invoice.getPayment());
		result.put("fullName", invoice.getBuyerName());
		result.put("email", session.getAttribute("loginId"));
		result.put("phoneNumber", invoice.getBuyerPhoneNumber());
		result.put("invoiceId", invoice.getId());
		
		String[] listStrAddress = address.split(" ");
		String addressLine1 = "".concat(listStrAddress[0]).concat(" ").concat(listStrAddress[1]);
		String addressLine2 = "";
		
		for(int i = 2; i < listStrAddress.length; i++ ) {
			addressLine2 = addressLine2.concat(listStrAddress[i]).concat(" ");
		}
		
		//★★★★★구매자 주소 1; addressLine1 ; 일반주소 
		result.put("addressLine1", addressLine1);
		//★★★★★구매자 주소 2; addressLine2 ; 일반주소 		
		result.put("addressLine2", addressLine2);
		
		return result;
	}
	
	@Operation(summary = "deleteInvoice() 해당 주문(인보이스) 삭제", description = "해당 주문(인보이스) 삭제; 결제 시도가 제대로 완료되지 않은 경우 DB에 기록이 남은 인보이스를 삭제하도록 지시")
	@Parameters({
		@Parameter(name = "<int> invoiceId", description = "주문(invoice) PK", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"기존 주문이 삭제되지 않았습니다. 관리자에게 문의하세요.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"주문 송장(Invoice)이 삭제되었습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/delete-invoice")
	public Map<String, Object> deleteInvoice(int invoiceId, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		
		InvoiceEntity invoice = invoiceBO.getEntityById(invoiceId);
		if(ObjectUtils.isEmpty(invoice)) {
			log.info("[InvoiceRestController deleteInvoice()] invoice failed to be found. invoice Id:{}", invoiceId);
			result.put("code", 500);
			result.put("error_message", "기존 주문이 삭제되지 않았습니다. 관리자에게 문의하세요.");
			return result;
		}
		
		invoiceBO.deleteInvoiceAndResetBuffer(invoice, (int)session.getAttribute("userId"));
				
		log.info("[InvoiceRestController deleteInvoice()] current invoice get Deleted. invoice Id:{}", invoiceId);
		result.put("code", 200);
		result.put("result", "주문 송장(Invoice)이 삭제되었습니다.");
		return result;
	}
	
	
	//TODO
	@PostMapping("/payment/complete")
	public Map<String, Object> paymentValidationWhenCompleted(@RequestParam String paymentId){
		Map<String, Object> result = new HashMap<>();
		
		//webclient로 kakaoApi 처럼 구현해야
		
		/*
		 //https://developers.portone.io/opi/ko/integration/start/v1/auth?v=v2
		  try {
		    // 요청의 body로 paymentId가 전달되기를 기대합니다.
		    const { paymentId, orderId } = req.body;
		
		    // 1. 포트원 결제내역 단건조회 API 호출
		    const paymentResponse = await fetch(
		      `https://api.portone.io/payments/${paymentId}`,
		      { headers: { Authorization: `PortOne ${PORTONE_API_SECRET}` } },
		    );
		    if (!paymentResponse.ok)
		      throw new Error(`paymentResponse: ${await paymentResponse.json()}`);
		    const payment = await paymentResponse.json();
		
		    // 2. 고객사 내부 주문 데이터의 가격과 실제 지불된 금액을 비교합니다.
		    const order = await OrderService.findById(orderId);
		    if (order.amount === payment.amount.total) {
		      switch (payment.status) {
		        case "VIRTUAL_ACCOUNT_ISSUED": {
		          // 가상 계좌가 발급된 상태입니다.
		          // 계좌 정보를 이용해 원하는 로직을 구성하세요.
		          break;
		        }
		        case "PAID": {
		          // 모든 금액을 지불했습니다! 완료 시 원하는 로직을 구성하세요.
		          break;
		        }
		      }
		    } else {
		      // 결제 금액이 불일치하여 위/변조 시도가 의심됩니다.
		    }
		  } catch (e) {
		    // 결제 검증에 실패했습니다.
		    res.status(400).send(e);
		  }
		 */
		
		
		return result;
	}
}
