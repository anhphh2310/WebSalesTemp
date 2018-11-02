package tma.datraining.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSales is a Querydsl query type for Sales
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSales extends EntityPathBase<Sales> {

    private static final long serialVersionUID = 1755959926L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSales sales = new QSales("sales");

    public final DateTimePath<java.sql.Timestamp> createAt = createDateTime("createAt", java.sql.Timestamp.class);

    public final NumberPath<java.math.BigDecimal> dollars = createNumber("dollars", java.math.BigDecimal.class);

    public final QLocation location;

    public final DateTimePath<java.sql.Timestamp> modifiedAt = createDateTime("modifiedAt", java.sql.Timestamp.class);

    public final QProduct product;

    public final ComparablePath<java.util.UUID> salesId = createComparable("salesId", java.util.UUID.class);

    public final QTime time;

    public QSales(String variable) {
        this(Sales.class, forVariable(variable), INITS);
    }

    public QSales(Path<? extends Sales> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSales(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSales(PathMetadata metadata, PathInits inits) {
        this(Sales.class, metadata, inits);
    }

    public QSales(Class<? extends Sales> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
        this.time = inits.isInitialized("time") ? new QTime(forProperty("time")) : null;
    }

}

