package com.alilopez.modules.reportes

import com.alilopez.modules.reportes.application.usecase.ActualizarEstadoUseCase
import com.alilopez.modules.reportes.application.usecase.ActualizarReporteUseCase
import com.alilopez.modules.reportes.application.usecase.CrearReporteUseCase
import com.alilopez.modules.reportes.application.usecase.EliminarReporteUseCase
import com.alilopez.modules.reportes.application.usecase.VerIdReporteUseCase
import com.alilopez.modules.reportes.application.usecase.VerReporteUseCase
import com.alilopez.modules.reportes.application.usecase.VerReportesPorUsuarioUseCase
import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.persistence.PostgresReporteRepository
import org.koin.dsl.module

val reporteModule = module {
    factory { VerReporteUseCase(get()) }
    factory { VerIdReporteUseCase(get()) }
    factory { CrearReporteUseCase(get()) }
    factory { ActualizarReporteUseCase(get()) }
    factory { EliminarReporteUseCase(get()) }
    factory { VerReportesPorUsuarioUseCase(get()) }
    factory { ActualizarEstadoUseCase(get()) }
    single<ReporteRepository> { PostgresReporteRepository() }

}