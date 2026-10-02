/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: {
        display: ["'Cormorant Garamond'", "serif"],
        body: ["'DM Sans'", "sans-serif"],
      },
      colors: {
        forest: {
          50:  "#f0f7f0",
          100: "#d9edd9",
          200: "#b3dbb3",
          300: "#7ec07e",
          400: "#4da04d",
          500: "#2d7d2d",
          600: "#1f6020",
          700: "#1a4e1b",
          800: "#163d17",
          900: "#0f2a10",
        },
        earth: {
          100: "#f5f0e8",
          200: "#e8dcc8",
          300: "#d4be9a",
          400: "#b8976a",
          500: "#8b6914",
        },
        cream: "#faf8f3",
      },
    },
  },
  plugins: [],
}