package kz.iitu.itse1910.issenbayev.repository.search;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

class AndSearchSpecBuilder<T> extends SearchSpecBuilder<T> {

    @Override
    Specification<T> combineSpecs(List<Specification<T>> specs) {
        Specification<T> finalSpec = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            finalSpec = Specification.where(finalSpec).and(specs.get(i));
        }
        return finalSpec;
    }
}
