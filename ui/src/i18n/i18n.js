import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import ro from './ro.json'
import en from './en.json'
import fr from './fr.json'

i18n
  .use(initReactI18next)
  .init({
    resources: {
      ro: { translation: ro },
      en: { translation: en },
      fr: { translation: fr },
    },
    lng: localStorage.getItem('lang') || 'ro',
    fallbackLng: 'ro',
    interpolation: { escapeValue: false },
  })

export default i18n