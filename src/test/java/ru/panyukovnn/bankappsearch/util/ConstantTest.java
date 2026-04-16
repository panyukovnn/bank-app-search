package ru.panyukovnn.bankappsearch.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class ConstantTest {

    public static final UUID PAGE_ENTITY_ID = UUID.fromString("e6556eb5-0ab3-421a-8adf-ef6ebf78e97c");

    public static final UUID PAGE_ENTITY_ID_2 = UUID.fromString("d8f0edc5-581e-45bf-91c9-3cfcdc01fe62");

    public static final String PAGE_NAME = "Перевод по номеру телефона";

    public static final String PAGE_NAME_2 = "Перевод по номеру карты";

    public static final String PLATFORM_VERSION = "1.22.3";

    public static final String IOS_PLATFORM = "ios";

    public static final String PAGE_LINK = "transfer/phone";

    public static final String PAGE_LINK_2 = "transfer/card";

    public static final String PAGE_DICTIONARY = "перевод телефон номер мобильный отправить";

    public static final String PAGE_DICTIONARY_2 = "перевод карта номер отправить деньги";

    public static final String PAGE_ICON = "ic_mobile_1";

    public static final String PAGE_ICON_2 = "ic_mobile_2";

    public static final OffsetDateTime DATE_TIME_NOW = OffsetDateTime.now();

    public static final String CLIENT_ID = "9fb5188b-35d5-4a80-994f-0d158bd260bd";

    public static final String SEARCH_STRING = "перевод по номеру";

    public static final String OPERATION_ID = "pay_001";

    public static final String OPERATION_NAME = "Оплата мобильной связи";

    public static final String OPERATION_CATEGORY_CODE = "COMMUNICATION";

    public static final String OPERATION_TYPE_CODE = "MOBILE";

    public static final String HISTORY_ID = "txn_001";

    public static final String HISTORY_NAME = "Перевод Олегу Т.";

    public static final BigDecimal AMOUNT = BigDecimal.valueOf(-500);

    public static final String RUB_CURRENCY = "RUB";

    public static final long MAX_LATEST_RESULTS = 2;

    public static final UUID EARLIEST_LATEST_RESULT_ENTITY_ID = UUID.fromString("f6e730a3-f191-41dc-b1c3-8878ebcd25a3");
}
