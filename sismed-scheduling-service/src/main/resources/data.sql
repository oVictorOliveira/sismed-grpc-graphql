INSERT INTO consulta (paciente_id, medico_id, data_hora, especialidade, observacoes, status, criado_por) VALUES
  (1, 10, DATEADD('DAY', 1, CURRENT_TIMESTAMP), 'Cardiologia', 'Consulta de rotina', 'AGENDADA', 'system'),
  (2, 10, DATEADD('DAY', -5, CURRENT_TIMESTAMP), 'Neurologia', 'Histórico de dor de cabeça', 'REALIZADA', 'system'),
  (1, 11, DATEADD('DAY', 7, CURRENT_TIMESTAMP), 'Ortopedia', 'Dor no joelho direito', 'CONFIRMADA', 'system');
