package com.ambev.projetopratico5;

import com.ambev.projetopratico5.dto.ProdutoDTO;
import com.ambev.projetopratico5.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestMongoConfig.class)
public class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String produtoJson;
    private ProdutoDTO produtoDTO ;
    private ProdutoDTO produtoDTOReturn;

    @BeforeEach
    public void setUp() throws Exception {
        produtoRepository.deleteAll();
    }

    @Test
    public void testCadastrarProdutoEVerificar() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoIT");
        produtoDTO.setDescricao("ProdutoIT descrição");
        produtoDTO.setPreco(11.99);

        produtoJson = objectMapper.writeValueAsString(produtoDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson))
                .andExpect(status().isOk())
                .andReturn();

        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/consultar/{nome}", produtoDTO.getNome()))
                .andExpect(status().isOk())
                .andReturn();

        String produtoRet = result.getResponse().getContentAsString();
        assert produtoRet.contains(produtoDTO.getNome());
    }

    @Test
    public void testDeletarUmProduto() throws Exception {


        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoIT");
        produtoDTO.setDescricao("ProdutoIT descrição");
        produtoDTO.setPreco(1.99);

        produtoJson = objectMapper.writeValueAsString(produtoDTO);

        MvcResult result = (MvcResult) mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson))
                .andExpect(status().isOk())
                .andReturn();

        result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{nome}", produtoDTO.getNome()))
                .andExpect(status().isOk())
                .andReturn();

        String ret = result.getResponse().getContentAsString();
        assert ret.contains("Produto deletado");

    }
    @Test
    public void testBuscarProdutoPeloNome() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoIT");
        produtoDTO.setDescricao("ProdutoIT descrição");
        produtoDTO.setPreco(11.99);

        produtoJson = objectMapper.writeValueAsString(produtoDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson))
                .andExpect(status().isOk())
                .andReturn();

        produtoDTO.setNome("Nome diferente!");

        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/consultar/{nome}", produtoDTO.getNome()))
                .andExpect(status().isNotFound())
                .andReturn();

        String ret = result.getResponse().getContentAsString();
        assert ret.isEmpty();
    }

}
