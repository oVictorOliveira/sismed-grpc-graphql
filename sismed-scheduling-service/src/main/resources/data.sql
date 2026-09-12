INSERT INTO consulta (paciente_id, medico_id, data_hora, especialidade, observacoes, status, criado_por) VALUES
  (1, 10, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY), 'Cardiologia', 'Consulta de rotina', 'AGENDADA', 'system'),
  (2, 10, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -5 DAY), 'Neurologia', 'Histórico de dor de cabeça', 'REALIZADA', 'system'),
  (1, 11, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 'Ortopedia', 'Dor no joelho direito', 'CONFIRMADA', 'system');
