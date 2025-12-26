/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: 'class',
    content: ["./src/main/resources/templates/**/*.html"],
    theme: {
        extend: {
            colors: {
                primary: '#8e1010',
                africanOrange: '#F59E0B',
                africanGreen: '#15803D',
                africanDark: '#0F172A',
            },
            keyframes: {
                fadeIn: {
                    '0%': { opacity: '0' },
                    '100%': { opacity: '1' },
                },
                slideUp: {
                    '0%': { transform: 'translateY(20px)', opacity: '0' },
                    '100%': { transform: 'translateY(0)', opacity: '1' },
                },
                slideInRight: {
                    '0%': { transform: 'translateX(20px)', opacity: '0' },
                    '100%': { transform: 'translateX(0)', opacity: '1' },
                },
            },
            animation: {
                fadeIn: 'fadeIn 0.5s ease-out forwards',
                slideUp: 'slideUp 0.5s ease-out forwards',
                slideInRight: 'slideInRight 0.5s ease-out forwards',
                'pulse-slow': 'pulse 3s cubic-bezier(0.4, 0, 0.6, 1) infinite',
            },
        },
    },
    plugins: [],
}
