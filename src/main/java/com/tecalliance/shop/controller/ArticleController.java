package com.tecalliance.shop.controller;

import com.tecalliance.shop.dto.ArticleDto;
import com.tecalliance.shop.dto.DiscountDto;
import com.tecalliance.shop.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDto>> search(@RequestParam Integer page, @RequestParam Integer size) {
        return Optional.ofNullable(articleService.search(page,size))
                .map(article -> ResponseEntity.ok(article.get().map(ArticleDto::fromEntity).collect(Collectors.toList())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getByName")
    public ResponseEntity<ArticleDto> getByName(@RequestParam String name) {
        return articleService.getByName(name)
                .map(article -> ResponseEntity.ok(ArticleDto.fromEntity(article)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArticleDto> save(@Valid @RequestBody ArticleDto data) {
        return ResponseEntity.ok(ArticleDto.fromEntity(articleService.save(data)));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> get(@PathVariable int id) {
        return articleService.getById(id)
                .map(article -> ResponseEntity.ok(ArticleDto.fromEntity(article)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/discount")
    public ResponseEntity<DiscountDto> saveNewDiscount(@PathVariable int id, @Valid @RequestBody DiscountDto data) {
        return ResponseEntity.ok(DiscountDto.fromEntity(articleService.saveNewDiscount(id, data)));
    }
}
