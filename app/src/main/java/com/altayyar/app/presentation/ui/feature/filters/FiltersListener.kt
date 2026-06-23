package com.altayyar.app.presentation.ui.feature.filters

import com.altayyar.app.entity.Filter

interface FiltersListener {
    fun deleteFilter(filter: Filter)
    fun updateFilter(updatedFilter: Filter)
}
