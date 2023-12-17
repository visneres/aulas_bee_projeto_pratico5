package com.ambev.projetopratico5.service;

import com.ambev.projetopratico5.dto.ProdutoDTO;
import com.ambev.projetopratico5.model.Produto;
import com.ambev.projetopratico5.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoDTO convertToDTO(Produto produto) {
        return modelMapper.map(produto, ProdutoDTO.class);
    }

    public Produto convertToProduto(ProdutoDTO produtoDTO) {
        return modelMapper.map(produtoDTO, Produto.class);
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public ProdutoDTO salvarProduto(ProdutoDTO produtoDTO) {
        Produto produto = convertToProduto(produtoDTO);
        return convertToDTO(produtoRepository.save(produto));
    }


    public Optional<Produto> findById(String id) {
        produtoRepository.findById(id);
        return produtoRepository.findById(id);
    }

    public List<ProdutoDTO> consultarPorNome(String nome) {
        List<Produto> listaProdutosPorNome = produtoRepository.findByNome(nome);
        List<ProdutoDTO> listaDTO = listaProdutosPorNome.stream()
                .map(source -> modelMapper.map(source, ProdutoDTO.class))
                .collect(Collectors.toList());
        return listaDTO;
    }

    public ProdutoDTO atualizarProduto(String id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id).
                orElse(null);
        if (produtoExistente != null) {
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setDescricao(produtoDTO.getDescricao());
            produtoExistente.setPreco(produtoDTO.getPreco());

            return convertToDTO(produtoRepository.save(produtoExistente));
        } else {
            return null;
        }
    }

    public boolean deletarProduto(String nome) {
        if (produtoRepository.findUmProdutoByNome(nome).isEmpty()) {
            return false;
        }else{
            produtoRepository.deleteByNome(nome);
            List<Produto> produto = produtoRepository.findUmProdutoByNome(nome);
            return true;
        }
    }

    public Object findAll() {
        List<Produto> listaProdutos = produtoRepository.findAll();
        List<ProdutoDTO> listaDTO = listaProdutos.stream()
                .map(source -> modelMapper.map(source, ProdutoDTO.class))
                .collect(Collectors.toList());
        return listaDTO;
    }
}
