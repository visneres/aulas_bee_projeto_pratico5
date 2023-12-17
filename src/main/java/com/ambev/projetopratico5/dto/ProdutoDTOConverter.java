package com.ambev.projetopratico5.dto;

import com.ambev.projetopratico5.model.Produto;
import org.modelmapper.ModelMapper;

public class ProdutoDTOConverter {
    private ModelMapper modelMapper = new ModelMapper();

    public ProdutoDTO convertToDTO(Produto produto) {
        return modelMapper.map(produto, ProdutoDTO.class);
    }
}
