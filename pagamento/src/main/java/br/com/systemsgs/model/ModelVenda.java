package br.com.systemsgs.model;

import br.com.systemsgs.vo.VendaVO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "venda")
public class ModelVenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "valorTotal", nullable = false)
    private BigDecimal valorTotal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venda", cascade = CascadeType.REFRESH )
    private List<ModelProdutoVenda> produtos;

    public static ModelVenda converteEntidade(VendaVO vendaVO){
        return new ModelMapper().map(vendaVO, ModelVenda.class);
    }

}
