package tma.datraining.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1677239257L;

    public static final QProduct product = new QProduct("product");

    public final StringPath classProduct = createString("classProduct");

    public final DateTimePath<java.sql.Timestamp> createAt = createDateTime("createAt", java.sql.Timestamp.class);

    public final StringPath inventory = createString("inventory");

    public final NumberPath<Integer> item = createNumber("item", Integer.class);

    public final DateTimePath<java.sql.Timestamp> modifiedAt = createDateTime("modifiedAt", java.sql.Timestamp.class);

    public final ComparablePath<java.util.UUID> productId = createComparable("productId", java.util.UUID.class);

    public final SetPath<Sales, QSales> sales = this.<Sales, QSales>createSet("sales", Sales.class, QSales.class, PathInits.DIRECT2);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

