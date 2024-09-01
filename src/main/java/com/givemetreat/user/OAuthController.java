package com.givemetreat.user;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.common.utils.EncryptUtils;
import com.givemetreat.user.bo.OAuthBO;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

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

@Tag(name = "OAuth Controller", description = "[Client] Open Authorization Controller; 현재는 카카오API만 구현되어 있음_22 08 2024")
@Slf4j
@RequiredArgsConstructor
@Controller
public class OAuthController {
	private final UserBO userBO;
	private final OAuthBO oAuthBO;

	@Operation(summary = "redirectedFromKakaoSignUp() 카카오 OAuth API 리다이렉트", description = "카카오 Open Authorization API")
	@Parameters({
			@Parameter(name = "<String> code", description = "코드")
			, @Parameter(name = "<String> error", description = "에러", example="access_denied")
			, @Parameter(name = "<String> error_description", description = "에러 관련 설명")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "\"redirect:/\" 등록 실패한 케이스, 메인 페이지로 리다이렉트")
		, @ApiResponse(responseCode = "200"
					, description = "\"redirect:/product/product-list-view\""
							+ "\n 성공한 경우 회원가입 및 로그인 처리 후 상품 메인페이지로 이동"
							+ " session에 &lt;int&gt; userId, &lt;String&gt; loginId, &lt;String&gt; userName 추가"
					, content = @Content(mediaType = "TEXT_HTML"))
	})
	@SuppressWarnings("unchecked")
	@GetMapping("/OAuth/kakao-sign-up")
	public String redirectedFromKakaoSignUp(
			@RequestParam(required = false) String code
			,@RequestParam(required = false) String error
			,@RequestParam(required = false) String error_description
			, HttpSession session
			){
		log.info("[OAuth: Kakao] code for Token:{}", code);
		
		//사용자가 카카오 관련정보 동의하지 않음; 회원가입 리롤
		if(error != null && error.equals("access_denied")) {
			log.info("[OAuthController : Kakao] access_denied. error:{}", error);
			return "redirect:/";
		}
		
		//2)Token as Response
		ResponseTokenKakaoApi TokenResponse = oAuthBO.postRequestWithCodeForToken(code);
		if(ObjectUtils.isEmpty(TokenResponse)) {
			log.info("[OAuthController : Kakao] TokenResponse is empty. TokenResponse:{}", TokenResponse);
			return "redirect:/";
		}
		
		//3)AccessToken
		String accessToken = TokenResponse.getAccess_token();
		UserInfoKakaoApi userInfo = oAuthBO.accessUserInfoWithAccessToken(accessToken);
		if(ObjectUtils.isEmpty(userInfo)) {
			log.info("[OAuthController : Kakao] userInfo is empty. userInfo:{}", userInfo);
			return "redirect:/";
		}
		
		String idUserInfo = userInfo.getId();
		String email = (String) userInfo.getKakao_account().get("email");
		Map<String, Object> profile = (Map<String, Object>) userInfo.getKakao_account().get("profile");
		String nickname  = (String) profile.get("nickname");
		
		if(ObjectUtils.isEmpty(idUserInfo)
				|| ObjectUtils.isEmpty(email)
				|| ObjectUtils.isEmpty(nickname)) {
			return "redirect:/";
		}
		
		//이미 이메일이 DB에 존재하는 경우, 비밀번호 일치하는지 확인
			//없다면 회원가입 절차 밟아야
		UserEntity userCur = userBO.getUserByLoginId(email);
		
		//회원가입 절차
		if(ObjectUtils.isEmpty(userCur)) {
			String salt = EncryptUtils.getSalt();
			String password = EncryptUtils.sha256(salt, idUserInfo);
			
			userCur = userBO.addUser(email, password, salt, nickname);
			//회원가입이 진행되지 않은 경우 다시 리턴
			if(ObjectUtils.isEmpty(userCur)) {
				return "redirect:/";
			}
		}
		
		//로그인 전 확인 절차;Sign-in Validation
		//salt 생성하고 userInfo의 id값을 합쳐서 비밀번호를 생성
		String msc = userCur.getSalt();
		String hashedPassword = EncryptUtils.sha256(msc, idUserInfo);
		
		if(hashedPassword.equals(userCur.getPassword()) == false ) {
			//비밀번호 불칠이 -> 로그인 실패;
			log.info("[OAuth: Kakao] current email failed to sign in. email:{}", email);
			return "redirect:/";
		}
		
		log.info("[OAuth: Kakao] OAuth sign-in validation get authorized.");
		//비밀번호 확인 후 로그인 절차
		session.setAttribute("userId", userCur.getId());
		session.setAttribute("loginId", userCur.getLoginId());
		session.setAttribute("userName", userCur.getNickname());
		return "redirect:/product/product-list-view";
	}
}
