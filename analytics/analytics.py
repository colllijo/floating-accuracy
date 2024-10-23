import pandas as pd
import json
import matplotlib.pyplot as plt


# Load the JSON file into a Pandas DataFrame
def load_data(json_file):
    with open(json_file, 'r') as file:
        data = json.load(file)

    # Extract the relevant information into a DataFrame
    results = []
    for entry in data:
        result = entry.get('result', {})
        double_result = float(result.get('doubleResult', 0))
        big_real_result = float(result.get('bigRealResult', 0))

        steps = entry.get('steps', [])
        for step in steps:
            results.append({
                "calculation": entry.get('calculation', ''),
                "doubleResult": double_result,
                "bigRealResult": big_real_result,
                "step_dValue": float(step.get('dValue', 0)),
                "step_bdValue": float(step.get('bdValue', 0)),
                "step_difference": float(step.get('difference', 0)),
                "overall_difference": double_result - big_real_result,
            })

    return pd.DataFrame(results)


# Function to create various plots
def create_statistical_graphics(df):
    # Bar plot of step differences
    plt.figure(figsize=(10, 6))
    df['step_difference'].plot(kind='bar', color='red', alpha=0.7)
    plt.title('Step Differences Between dValue and bdValue')
    plt.xlabel('Step Index')
    plt.ylabel('Difference (E-17 scale)')
    plt.tight_layout()
    plt.savefig('media/images/step_difference_plot.png')  # Updated path
    plt.close()

    # Histogram of step_dValue and step_bdValue
    plt.figure(figsize=(10, 6))
    plt.hist(df['step_dValue'], bins=10, alpha=0.5, label='dValue', color='blue')
    plt.hist(df['step_bdValue'], bins=10, alpha=0.5, label='bdValue', color='green')
    plt.title('Histogram of dValue and bdValue')
    plt.xlabel('Value')
    plt.ylabel('Frequency')
    plt.legend()
    plt.tight_layout()
    plt.savefig('media/images/histogram_values.png')  # Updated path
    plt.close()

    # Scatter plot comparing doubleResult and bigRealResult
    plt.figure(figsize=(10, 6))
    plt.scatter(df['doubleResult'], df['bigRealResult'], alpha=0.7, color='purple')
    plt.title('Comparison of doubleResult vs. bigRealResult')
    plt.xlabel('doubleResult')
    plt.ylabel('bigRealResult')
    plt.tight_layout()
    plt.savefig('media/images/scatter_comparison.png')  # Updated path
    plt.close()

    # Line plot for step values over their index
    plt.figure(figsize=(10, 6))
    plt.plot(df.index, df['step_dValue'], marker='o', label='dValue', color='blue')
    plt.plot(df.index, df['step_bdValue'], marker='x', label='bdValue', color='green')
    plt.title('dValue and bdValue over Steps')
    plt.xlabel('Step Index')
    plt.ylabel('Value')
    plt.legend()
    plt.tight_layout()
    plt.savefig('media/images/line_plot_values.png')  # Updated path

    print("Plots saved in media/images as step_difference_plot.png, histogram_values.png, scatter_comparison.png, line_plot_values.png")



# Main function to run the analysis
if __name__ == "__main__":
    json_file = 'calc.json'  # Replace with your JSON file path
    df = load_data(json_file)
    create_statistical_graphics(df)
