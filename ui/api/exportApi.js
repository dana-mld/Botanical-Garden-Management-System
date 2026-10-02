import axios from 'axios'

const authHeader = () => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
}

export const exportApi = {
  exportPlants: (format) =>
    axios.get(`/api/export/plants`, {
      params: { format },
      headers: authHeader(),
      responseType: 'blob',
    }),
  exportUsers: () =>
    axios.get(`/api/export/users`, {
      headers: authHeader(),
      responseType: 'blob',
    }),
  getStatistics: () =>
    axios.get('/api/statistics', { headers: authHeader() }),
  saveStatisticsWord: () =>
    axios.get('/api/statistics/export', {
      headers: authHeader(),
      responseType: 'blob',
    }),
}