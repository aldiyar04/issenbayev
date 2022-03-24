package kz.iitu.itse1910.issenbayev.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
class SearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private final Object value;
}
