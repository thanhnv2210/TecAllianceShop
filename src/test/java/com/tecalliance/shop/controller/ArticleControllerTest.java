package com.tecalliance.shop.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tecalliance.shop.dto.ArticleDto;
import com.tecalliance.shop.dto.DiscountDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleControllerTest {
    static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    static Gson gsonCommon = new GsonBuilder()
            //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .setDateFormat("yyyy-MM-dd")
            .create();
    @Autowired
    private MockMvc mockMvc;
    static final SimpleDateFormat fs = new SimpleDateFormat("yyyy-MM-dd");

    static final String specialArticle = "ART_DISCOUNT_5";
    static final String specialSlogan = "AD_SLOGAN_1";
    static Integer currentArticleId;
    static Double currentMaxDiscountPercent;
    Random r = new Random();

    @Order(1)
    @DisplayName("[01(H)] Post valid Article")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource("providerValidArticleForPost")
    public void whenPostAnArticle_thenRetrievedSuccessStatus(ArticleDto data) throws Exception {
        String url = "/articles";
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Order(2)
    @DisplayName("[02.1(UH)] Post an exist Article")
    @Test
    public void whenPostAnExistArticle_thenRetrievedConflictStatus() throws Exception {
        ArticleDto data = new ArticleDto(specialArticle,specialSlogan,20D, 40D, 0.07D);
        String url = "/articles";
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isConflict());
    }

    @Order(2)
    @DisplayName("[02.2(UH)] Post an invalid Article")
    @Test
    public void whenPostAnInvalidArticle_thenRetrievedBadRequestStatus() throws Exception {
        ArticleDto data = new ArticleDto(specialArticle,specialSlogan,null, 40D, 0.07D);
        String url = "/articles";
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Order(3)
    @DisplayName("[03(H)] Get special ArticleId")
    @Test
    public void whenGetASpecialArticle_thenRetrievedSuccessStatus() throws Exception {
        String url = "/articles/getByName?name=" + specialArticle;
        MvcResult result = this.mockMvc.perform(get(url).contentType(APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isOk()).andReturn();        ;
        ArticleDto article = gsonCommon.fromJson(result.getResponse().getContentAsString(), ArticleDto.class);
        System.out.println("article:" + article);
        if(article != null) {
            currentMaxDiscountPercent = article.getMaxDiscountPercent();
            currentArticleId = article.getId();
        }
    }

    @Order(4)
    @DisplayName("[04(H)] Post valid Discount")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource("providerValidDiscountForPost")
    public void whenPostADiscount_thenRetrievedSuccessStatus(DiscountDto data) throws Exception {
        String url = "/articles/" + currentArticleId + "/discount";
        data.setPercent(currentMaxDiscountPercent * r.nextDouble());
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Order(5)
    @DisplayName("[05.1(UH)] Post an invalid Discount Date")
    @Test
    public void whenPostAInvalidDiscount_thenRetrievedBadRequestStatus() throws Exception {
        String url = "/articles/" + currentArticleId + "/discount";
        DiscountDto data = new DiscountDto(null,fs.parse("2021-05-01"),10D);
        data.setPercent(currentMaxDiscountPercent + (100D - currentMaxDiscountPercent) * r.nextDouble());
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Order(6)
    @DisplayName("[05.2(UH)] Post an out of range of Discount")
    @Test
    public void whenPostAInvalidDiscountPercent_thenRetrievedBadRequestStatus() throws Exception {
        String url = "/articles/" + currentArticleId + "/discount";
        DiscountDto data = new DiscountDto(fs.parse("2021-04-01"),fs.parse("2021-05-01"),10D);
        data.setPercent(currentMaxDiscountPercent + (100D - currentMaxDiscountPercent) * r.nextDouble());
        this.mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(gsonCommon.toJson(data)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Out of discount range")));
    }

    private static Stream<Arguments> providerValidArticleForPost() {
        return Stream.of(
                Arguments.of(new ArticleDto("ART_1","SLOGAN_1",10D, 20D, 0.07D)),
                Arguments.of(new ArticleDto("ART_2","SLOGAN_2",20D, 30D, 0.07D)),
                Arguments.of(new ArticleDto(specialArticle,specialSlogan,20D, 40D, 0.07D))
        );
    }

    private static Stream<Arguments> providerValidDiscountForPost() throws ParseException {
        return Stream.of(
                Arguments.of(new DiscountDto(fs.parse("2021-04-01"),fs.parse("2021-05-01"),10D))
        );
    }
}
