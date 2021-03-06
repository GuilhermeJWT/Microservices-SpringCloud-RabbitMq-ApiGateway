package br.com.systemsgs.serivice;

import br.com.systemsgs.config.ProdutoSendMessage;
import br.com.systemsgs.exception.ResourceNotFoundException;
import br.com.systemsgs.model.ModelProduto;
import br.com.systemsgs.repository.ProdutoRepository;
import br.com.systemsgs.vo.ProdutoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoSendMessage produtoSendMessage;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
        this.produtoRepository = produtoRepository;
        this.produtoSendMessage = produtoSendMessage;
    }

    @Transactional
    public ProdutoVO create(ProdutoVO produtoVO){
        ProdutoVO produtoRetorno = ProdutoVO.converteEntidade(produtoRepository.save(ModelProduto.coverteEntidade(produtoVO)));
        produtoSendMessage.sendMessage(produtoRetorno);
        return produtoRetorno;
    }

    public ProdutoVO findById(Long id){
        var produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Produto não Encontrado!"));

        return ProdutoVO.converteEntidade(produto);
    }

    public ProdutoVO alterar(ProdutoVO produtoVO){
        final Optional<ModelProduto> optionalProduto = produtoRepository.findById(produtoVO.getId());

        if (!optionalProduto.isPresent()){
            new ResourceNotFoundException("Produto não Encontrado!");
        }

        return ProdutoVO.converteEntidade(produtoRepository.save(ModelProduto.coverteEntidade(produtoVO)));
    }

    public void delete(Long id){
        var produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não Encontrado!"));
        produtoRepository.delete(produto);
    }

    public Page<ProdutoVO> findAll(Pageable pageable){
        var page = produtoRepository.findAll(pageable);

        return page.map(this::convertToProdutoVO);
    }

    private ProdutoVO convertToProdutoVO(ModelProduto modelProduto){
        return ProdutoVO.converteEntidade(modelProduto);
    }

}
