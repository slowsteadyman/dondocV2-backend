package com.dondoc.repository;

import com.dondoc.dto.Categories;
import com.dondoc.dto.Records;
import com.dondoc.entity.QCategory;
import com.dondoc.entity.QRecorde;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class RecordRepositoryImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RecordRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Records.ItemResponse> findByUserMonth(Long userId, String yearMonth, String type) {
        QRecorde r = QRecorde.recorde;
        QCategory c = QCategory.category;

        JPAQuery<Tuple> query = queryFactory
            .select(r.id, r.amount, r.description, r.memo, r.recordDate,
                c.id, c.name, c.type)
            .from(r)
            .join(c).on(r.categoryId.eq(c.id))
            .where(
                r.userId.eq(userId),
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", r.recordDate).eq(yearMonth),
                type != null ? c.type.equalsIgnoreCase(type) : null
            );

        return query.fetch().stream()
                .map(tuple -> new Records.ItemResponse(
                        tuple.get(r.id),
                        tuple.get(c.type).toUpperCase(),
                        tuple.get(r.recordDate).toString(),
                        new Categories.Info(tuple.get(c.id), tuple.get(c.name)),
                        tuple.get(r.amount),
                        tuple.get(r.description),
                        tuple.get(r.memo)
                ))
                .toList();
    }

    @Override
    public Records.Summary findSummaryByUserMonth(Long userId, String yearMonth, String type) {
        QRecorde r = QRecorde.recorde;
        QCategory c = QCategory.category;

        JPAQuery<Tuple> query = queryFactory
            .select(
                c.type.when("INCOME").then(r.amount).otherwise(0L).sum(),
                c.type.when("EXPENSE").then(r.amount).otherwise(0L).sum()
            )
            .from(r)
            .join(c).on(r.categoryId.eq(c.id))
            .where(
                r.userId.eq(userId),
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", r.recordDate).eq(yearMonth),
                type != null ? c.type.equalsIgnoreCase(type) : null
            );

        Tuple result = query.fetchOne();
        long totalIncome = result != null && result.get(0, Long.class) != null ? result.get(0, Long.class) : 0L;
        long totalExpense = result != null && result.get(1, Long.class) != null ? result.get(1, Long.class) : 0L;

        return new Records.Summary(totalIncome, totalExpense, totalIncome - totalExpense);
    }
}
