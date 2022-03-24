package kz.iitu.itse1910.issenbayev.repository.search;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SearchSpecBuilder<T> {
    private final List<SearchCriteria> criteria = new ArrayList<>();

    public SearchSpecBuilder<T> with(String key, SearchOperation operation, Object value) {
        criteria.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (criteria.size() == 0) {
            throw new IllegalStateException("No criteria specified, unable to build search specification");
        }

        List<Specification<T>> specs = criteria.stream()
                .map(SearchSpec<T>::new)
                .collect(Collectors.toList());

        return combineSpecs(specs);
    }

    abstract Specification<T> combineSpecs(List<Specification<T>> specs);
}
