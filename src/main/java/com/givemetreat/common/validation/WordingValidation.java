package com.givemetreat.common.validation;

import java.util.regex.Pattern;

public class WordingValidation {

	/**
	 * ID 확인용; 
	 * @param String
	 * @return
	 */
	public static boolean hasAlphaNumericLengthBetween8And16(String str) {
		return str.length() > 8
				&& str.length() < 16
				&& Pattern.matches("[a-zA-Z0-9]*$", str);
	}
	
	/**
	 * 비밀번호 확인용; 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야
	 * @param String
	 * @return
	 */
	public static boolean hasAlphaLowerUpperNumericSpecialLengthBetween8And16(String str){
		return Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", str);
	}
	
	public static boolean hasLiteralsOnly(String str){
		return Pattern.matches("[!@#$%^&*]", str) == false;
	}
	
    // 숫자 검사기
    public static boolean isNumeric(String str) {
        return Pattern.matches("^[0-9]*$", str);
    }

    // 영어 검사기
    public static boolean isAlpha(String str) {
        return Pattern.matches("^[a-zA-Z]*$", str);
    }

    // 한국어 혹은 영어 검사기
    public static boolean isAlphaNumeric(String str) {
        return Pattern.matches("[a-zA-Z0-9]*$", str);
    }

    // 한국어 검사기
    public static boolean isKorean(String str) {
        return Pattern.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣0-9!?()]+.*", str.replace("\n",""));
    }

    // 대문자 검사기
    public static boolean isUpper(String str) {
        return Pattern.matches("^[A-Z]*$", str);
    }

    // 소문자 검사기
    public static boolean isDowner(String str) {
        return Pattern.matches("^[a-z]*$", str);
    }

    /**
     * 이메일 양식 체크
     * @param stringEmail
     * @return
     */
    public static boolean isEmail(String stringEmail) {
        return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", stringEmail);
    }

    // URL 검사기 (Http, Https 제외)
    public static boolean isURL(String str) {
        return Pattern.matches("^[^((http(s?))\\:\\/\\/)]([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$",
                str);
    }

    // phone number checker
    // xxx-xxx-xxxx (형식만 비교)
    // - 은 없어야 함.
    public static boolean isMobilePhone(String str) {
        return Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", str);
    }

    // 일반 전화 검사기
    public static boolean isPhone(String str) {
        return Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", str);
    }

    // IP 검사기
    public static boolean isIp(String str) {
        return Pattern.matches("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})", str);
    }

    // 우편번호 검사기
    public static boolean isPost(String str) {
        return Pattern.matches("^\\d{3}\\d{2}$", str);
    }

    // 주민등록번호 검사기
    public static boolean isPersonalID(String str) {
        return Pattern.matches("^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$", str);
    }

	// 한자를 포함하고 있는지 여부
    public static boolean containsChineseCharacters(String str){
        String regEx = ".*[\u2e80-\u2eff\u31c0-\u31ef\u3200-\u32ff\u3400-\u4dbf\u4e00-\u9fbf\uf900-\ufaff].*";
        return str.matches(regEx);
    }

    // 신용 카드 양식(xxxx-xxxx-xxxx-xxxx)
    public static boolean isCreditCard(String cardNumber) {
        return Pattern.matches("((5[1-5]\\d{14})|(4\\d{12}(\\d{3})?)|(3[47]\\d{13})|(6011\\d{12})|((30[0-5]|3[68]\\d)\\d{11}))", cardNumber);
    }

    // 신용카드 유효기간 양식
    public static boolean isExpireDate(String expireDate) {
        return Pattern.matches("[0-9]{2}(?:0[1-9]|1[0-2])", expireDate);
    }
}
