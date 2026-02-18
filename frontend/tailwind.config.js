/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#111827",
        slate: "#1f2937",
        sand: "#f8f4ef",
        accent: "#f97316",
        mint: "#10b981"
      }
    }
  },
  plugins: []
};
