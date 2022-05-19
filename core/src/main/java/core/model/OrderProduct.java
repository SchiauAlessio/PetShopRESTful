package core.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderProduct extends BaseEntity<OrderProductKey> {
    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Orders order;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;
}
