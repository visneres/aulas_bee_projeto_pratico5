package com.ambev.projetopratico5.controller;

import com.ambev.projetopratico5.dto.ProdutoDTO;
import com.ambev.projetopratico5.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {


    @Autowired
    private ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoDTO cadastrarProduto(@RequestBody @Valid ProdutoDTO produto) {
        return produtoService.salvarProduto(produto);
    }

    @GetMapping("/consultar/{nome}")
    public ResponseEntity<List<ProdutoDTO>> consultarProdutos(@PathVariable String nome) {
        List<ProdutoDTO> produtos = produtoService.consultarPorNome(nome);
        if (produtos != null) {
            return ResponseEntity.ok(produtos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/consultar")
    public ResponseEntity<List<ProdutoDTO>> consultarProdutos() {
        List<ProdutoDTO> produtos = (List<ProdutoDTO>) produtoService.findAll();
        if (produtos != null) {
            return ResponseEntity.ok(produtos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable String id,
                                                       @RequestBody ProdutoDTO produtoDTO){
        ProdutoDTO produtoAtualizado = produtoService.atualizarProduto
                (id, produtoDTO);
        if(produtoAtualizado != null){
            return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<String> deletarProduto(@PathVariable String nome){
        boolean deletado =  produtoService.deletarProduto(nome);
        if(deletado){
            return ResponseEntity.ok("Produto deletado");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado!");
        }
    }
}
