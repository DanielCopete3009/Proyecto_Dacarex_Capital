package com.dacarex.capital;

import com.dacarex.capital.auth.CredencialesLogin;
import com.dacarex.capital.auth.RegistroUsuario;
import com.dacarex.capital.auth.SesionUsuario;
import com.dacarex.capital.dashboard.ResumenFinanciero;
import com.dacarex.capital.enums.*;
import com.dacarex.capital.exception.DacarexException;
import com.dacarex.capital.informe.ParametrosInforme;
import com.dacarex.capital.informe.ResultadoInforme;
import com.dacarex.capital.io.ConfiguracionApp;
import com.dacarex.capital.io.Logger;
import com.dacarex.capital.model.*;
import com.dacarex.capital.service.*;

import java.time.LocalDate;
import java.util.List;

public class Main {

    // Servicios globales de la aplicación
    private static AuthService        authService;
    private static CategoriaService   categoriaService;
    private static MovimientoService  movimientoService;
    private static InformeService     informeService;

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       DACAREX CAPITAL  v1.0          ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        try {
            // ─────────────────────────────────────
            // 1. INICIALIZACIÓN
            // ─────────────────────────────────────
            inicializar();

            // ─────────────────────────────────────
            // 2. REGISTRO Y LOGIN
            // ─────────────────────────────────────
            SesionUsuario sesion = probarAutenticacion();

            // ─────────────────────────────────────
            // 3. CATEGORÍAS
            // ─────────────────────────────────────
            probarCategorias();

            // ─────────────────────────────────────
            // 4. MOVIMIENTOS
            // ─────────────────────────────────────
            probarMovimientos();

            // ─────────────────────────────────────
            // 5. DASHBOARD
            // ─────────────────────────────────────
            probarDashboard();

            // ─────────────────────────────────────
            // 6. INFORMES
            // ─────────────────────────────────────
            probarInformes();

            // ─────────────────────────────────────
            // 7. FILTROS
            // ─────────────────────────────────────
            probarFiltros();

            // ─────────────────────────────────────
            // 8. VALIDACIONES (errores controlados)
            // ─────────────────────────────────────
            probarValidaciones();

            // ─────────────────────────────────────
            // 9. LOGOUT
            // ─────────────────────────────────────
            authService.logout();
            System.out.println("\n✔ Sesión cerrada correctamente.");
            Logger.info("Aplicación cerrada correctamente.");

        } catch (DacarexException e) {
            System.err.println("\n✘ Error de aplicación: " + e.getMessage());
            Logger.error("Error fatal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n✘ Error inesperado: " + e.getMessage());
            Logger.error("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Fin de la ejecución          ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────────────────────────
    // INICIALIZACIÓN
    // ─────────────────────────────────────────────────────────────
    private static void inicializar() {
        System.out.println("── Inicializando servicios...");

        // Cargar configuración (crea config.properties si no existe)
        ConfiguracionApp config = new ConfiguracionApp();
        System.out.println("   App:     " + config.getAppNombre() + " " + config.getAppVersion());
        System.out.println("   Moneda:  " + config.getAppMoneda());

        // Intentar conexión a BD (opcional en este paso)
        try {
            com.dacarex.capital.db.ConexionBD.getInstance();
            System.out.println("   BD:      ✔ Conectado a MySQL");
            Logger.info("Conexión a MySQL establecida.");
        } catch (DacarexException e) {
            System.out.println("   BD:      ⚠ Sin conexión MySQL (modo memoria)");
            Logger.advertencia("Sin conexión a MySQL. Modo memoria activado.");
        }

        // Inicializar servicios
        authService       = new AuthService();
        categoriaService  = new CategoriaService();
        movimientoService = new MovimientoService(categoriaService);
        informeService    = new InformeService(movimientoService);

        // Cargar categorías iniciales
        categoriaService.cargarCategoriasIniciales();

        Logger.info("Servicios inicializados correctamente.");
        System.out.println("   Servicios inicializados correctamente.\n");
    }

    // ─────────────────────────────────────────────────────────────
    // AUTENTICACIÓN
    // ─────────────────────────────────────────────────────────────
    private static SesionUsuario probarAutenticacion() {
        System.out.println("── Autenticación");

        // Registro
        RegistroUsuario registro = new RegistroUsuario(
            "Carlos García", "carlos@dacarex.com",
            "segura1234", "segura1234",
            TipoCuenta.EMPRESA, "Dacarex S.L."
        );
        System.out.println("   Errores registro: " +
            (registro.esValido() ? "ninguno ✔" : registro.validar()));

        Usuario usuario = authService.registrar(registro);
        System.out.println("   Usuario creado:   " + usuario.getNombreCompleto());

        // Login
        CredencialesLogin credenciales = new CredencialesLogin(
            "carlos@dacarex.com", "segura1234"
        );
        SesionUsuario sesion = authService.login(credenciales);
        System.out.println("   Sesión activa:    " + sesion.estaActiva() + " ✔");
        System.out.println("   Token:            " + sesion.getToken().substring(0, 8) + "...\n");

        Logger.info("Login correcto: " + usuario.getEmail());
        return sesion;
    }

    // ─────────────────────────────────────────────────────────────
    // CATEGORÍAS
    // ─────────────────────────────────────────────────────────────
    private static void probarCategorias() {
        System.out.println("── Categorías");

        List<Categoria> todas = categoriaService.buscarTodas();
        System.out.println("   Total cargadas: " + todas.size());

        List<Categoria> gastos   = categoriaService.buscarPorTipo(TipoMovimiento.GASTO);
        List<Categoria> ingresos = categoriaService.buscarPorTipo(TipoMovimiento.INGRESO);
        System.out.println("   Gastos:   " + gastos.size());
        System.out.println("   Ingresos: " + ingresos.size());

        // Crear una nueva
        FormularioCategoria formCat = new FormularioCategoria("Tecnología", TipoMovimiento.GASTO);
        Categoria nueva = categoriaService.crear(formCat);
        System.out.println("   Nueva categoría: " + nueva.getNombre() + " ✔\n");

        Logger.info("Categorías cargadas: " + categoriaService.buscarTodas().size());
    }

    // ─────────────────────────────────────────────────────────────
    // MOVIMIENTOS
    // ─────────────────────────────────────────────────────────────
    private static void probarMovimientos() {
        System.out.println("── Movimientos");

        // Buscar ids de categorías
        String idNomina    = categoriaService.buscarPorTipo(TipoMovimiento.INGRESO)
                                .get(0).getId();
        String idAlquiler  = categoriaService.buscarPorTipo(TipoMovimiento.GASTO)
                                .stream()
                                .filter(c -> c.getNombre().equals("Alquiler"))
                                .findFirst().get().getId();
        String idSuministros = categoriaService.buscarPorTipo(TipoMovimiento.GASTO)
                                .stream()
                                .filter(c -> c.getNombre().equals("Suministros"))
                                .findFirst().get().getId();

        // Crear movimientos
        Movimiento m1 = movimientoService.crear(new FormularioMovimiento(
            TipoMovimiento.INGRESO, "Nómina marzo",
            3500.00, idNomina,
            LocalDate.of(2026, 3, 1), null
        ));

        Movimiento m2 = movimientoService.crear(new FormularioMovimiento(
            TipoMovimiento.GASTO, "Alquiler oficina",
            800.00, idAlquiler,
            LocalDate.of(2026, 3, 5), "Mensual"
        ));

        Movimiento m3 = movimientoService.crear(new FormularioMovimiento(
            TipoMovimiento.INGRESO, "Ventas abril",
            2800.00, idNomina,
            LocalDate.of(2026, 4, 2), null
        ));

        Movimiento m4 = movimientoService.crear(new FormularioMovimiento(
            TipoMovimiento.GASTO, "Internet fibra",
            49.99, idSuministros,
            LocalDate.of(2026, 4, 5), "Fibra 600Mb"
        ));

        System.out.println("   " + m1.getDescripcion() + " → +" + m1.getImporte() + "€");
        System.out.println("   " + m2.getDescripcion() + " → -" + m2.getImporte() + "€");
        System.out.println("   " + m3.getDescripcion() + " → +" + m3.getImporte() + "€");
        System.out.println("   " + m4.getDescripcion() + " → -" + m4.getImporte() + "€");
        System.out.println("   Saldo actual: " + movimientoService.getSaldoActual() + "€ ✔\n");

        Logger.info("Movimientos creados: " + movimientoService.contarMovimientos());
    }

    // ─────────────────────────────────────────────────────────────
    // DASHBOARD
    // ─────────────────────────────────────────────────────────────
    private static void probarDashboard() {
        System.out.println("── Dashboard");

        ResumenFinanciero resumen = movimientoService.calcularResumen(PeriodoDashboard.MES);
        System.out.println("   Periodo:    " + resumen.getPeriodo().getValor());
        System.out.println("   Ingresos:   " + resumen.getTotalIngresos() + "€");
        System.out.println("   Gastos:     " + resumen.getTotalGastos() + "€");
        System.out.println("   Flujo caja: " + resumen.getFlujoDeCaja() + "€");
        System.out.println("   Estado:     " + resumen.getEstadoFinanciero() + " ✔\n");
    }

    // ─────────────────────────────────────────────────────────────
    // INFORMES
    // ─────────────────────────────────────────────────────────────
    private static void probarInformes() {
        System.out.println("── Informes");

        ParametrosInforme params = new ParametrosInforme(PeriodoInforme.TRIMESTRAL, null, null);
        ResultadoInforme informe = informeService.generar(params);

        System.out.println("   Periodo:       " + informe.getParametros().getPeriodo().getValor());
        System.out.println("   Saldo acumulado: " + informe.getKpis().getSaldoAcumulado() + "€");
        System.out.println("   Mayor ingreso:   " + informe.getKpis().getMayorIngresoRegistrado() + "€");
        System.out.println("   Mayor gasto:     " + informe.getKpis().getMayorGastoRegistrado() + "€");
        System.out.println("   Cat. más activa: " + informe.getKpis().getCategoriaConMasMovimientos());
        System.out.println("   Filas resumen:   " + informe.getTablaResumen().size() + " ✔\n");
    }

    // ─────────────────────────────────────────────────────────────
    // FILTROS
    // ─────────────────────────────────────────────────────────────
    private static void probarFiltros() {
        System.out.println("── Filtros");

        // Solo ingresos
        FiltrosMovimientos soloIngresos = new FiltrosMovimientos(
            null, null, null, null, TipoMovimiento.INGRESO
        );
        List<Movimiento> ingresos = movimientoService.filtrar(soloIngresos);
        System.out.println("   Solo ingresos: " + ingresos.size() + " movimientos");

        // Por texto
        FiltrosMovimientos porTexto = new FiltrosMovimientos(
            "alquiler", null, null, null, null
        );
        List<Movimiento> resultado = movimientoService.filtrar(porTexto);
        System.out.println("   Búsqueda 'alquiler': " + resultado.size() + " movimiento(s)");

        // Por rango de fechas
        FiltrosMovimientos porFecha = new FiltrosMovimientos(
            null,
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            null, null
        );
        List<Movimiento> abril = movimientoService.filtrar(porFecha);
        System.out.println("   Abril 2026: " + abril.size() + " movimientos ✔\n");
    }

    // ─────────────────────────────────────────────────────────────
    // VALIDACIONES
    // ─────────────────────────────────────────────────────────────
    private static void probarValidaciones() {
        System.out.println("── Validaciones (errores controlados)");

        // Formulario vacío
        FormularioMovimiento formVacio = new FormularioMovimiento();
        System.out.println("   Formulario vacío:     " + formVacio.validar());

        // Contraseña incorrecta
        CambioContrasenia cambio = new CambioContrasenia("actual", "corta", "corta");
        System.out.println("   Cambio contraseña:    " + cambio.validar());

        // Login con credenciales erróneas
        try {
            authService.login(new CredencialesLogin("noexiste@x.com", "1234"));
        } catch (DacarexException e) {
            System.out.println("   Login erróneo capturado: " + e.getMessage() + " ✔");
        }

        // Registro con email duplicado
        try {
            authService.registrar(new RegistroUsuario(
                "Otro", "carlos@dacarex.com",
                "12345678", "12345678",
                TipoCuenta.PERSONAL, null
            ));
        } catch (DacarexException e) {
            System.out.println("   Email duplicado capturado: " + e.getMessage() + " ✔\n");
        }
    }
}