/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        primary: '#1a73e8', // 主色调，可调整匹配广财UI
        secondary: '#fbbc04',
        success: '#34a853',
        danger: '#ea4335',
      },
      boxShadow: {
        card: '0 2px 8px rgba(0, 0, 0, 0.1)',
      }
    },
  },
  plugins: [],
};
