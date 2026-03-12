Feature: Registrar gasto

  # ESCENARIO 1
  Scenario: Registrar un gasto asociado a un encargo
    Given existe un encargo activo
    When el personal registra un gasto completo
    Then el gasto queda registrado
    And el gasto queda asociado al encargo

  # ESCENARIO 2
  Scenario Outline: Calcular importe total con IVA
    Given el personal introduce un importe base de <base> euros
    And selecciona un IVA del <iva> por ciento
    Then el sistema calcula un total de <total> euros
    Examples:
      | base | iva | total |
      | 100  | 21  | 121   |
      | 100  | 10  | 110   |
      | 100  | 0   | 100   |

  # ESCENARIO 3
  Scenario: Intentar registrar gasto con importe 0
    Given existe un encargo activo
    When el personal registra un gasto con importe base 0
    Then el sistema muestra un mensaje de error
    And el gasto no se guarda
    And el mensaje indica que el importe base debe ser mayor que 0

  # ESCENARIO 4
  Scenario: Registrar gasto sin encargo
    When el personal intenta registrar un gasto sin encargo
    Then el sistema muestra un mensaje de error
    And el gasto no se guarda
    And el mensaje indica que debe existir un encargo activo

  # ESCENARIO 5
  Scenario: Registrar gasto sin fecha
    Given existe un encargo activo
    When el personal registra un gasto sin fecha
    Then el sistema muestra un mensaje de error
    And el gasto no se guarda
    And el mensaje indica que la fecha es obligatoria

  # ESCENARIO 6
  Scenario: Registrar gasto sin concepto
    Given existe un encargo activo
    When el personal registra un gasto sin concepto
    Then el sistema muestra un mensaje de error
    And el gasto no se guarda
    And el mensaje indica que el concepto es obligatorio

  # ESCENARIO 7
  Scenario: Seleccionar tipo de IVA
    Given existen tipos de IVA configurados
    When el personal inicia el registro de un gasto
    Then el sistema muestra una lista de tipos de IVA disponibles
