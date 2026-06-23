package com.altayyar.app.components.filters

import com.altayyar.app.entity.Filter

interface FiltersListener {
    fun deleteFilter(filter: Filter)
    fun updateFilter(updatedFilter: Filter)
}
