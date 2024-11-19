import sys
import pandas as pd
import json
import matplotlib.pyplot as plt

def load_json(json_file):
    with(open(json_file, 'r')) as file:
        data = json.load(file)
    return data

def normalize_calculation_data(data):
    entries = []
    for entry in data:
        entries.append({
            'dValue': entry['result']['doubleResult'],
            'bValue': entry['result']['bigRealResult'],
            'diff': entry['result']['resultDifference']
        })

    df = pd.DataFrame(entries)

    df['diff'] = pd.to_numeric(df['diff'], errors='coerce')
    df['dValue'] = pd.to_numeric(df['dValue'], errors='coerce')
    df['bValue'] = pd.to_numeric(df['bValue'], errors='coerce')

    return df

# Calculation based analytics

def plot_all_diferences(df):
    # NOTE: Should this graph use absolute differences?
    plt.plot(df['diff'].dropna())
    plt.title('Alle Differenzen')
    plt.xlabel('Index')
    plt.ylabel('Differenz')
    plt.grid(True)
    plt.savefig('media/images/all_differences.png')
    plt.close()

def plot_histogram(df):
    # NOTE: What is a good bin size?
    plt.hist(df['diff'].dropna(), bins=42)
    plt.title('Histogram der Differenzen')
    plt.xlabel('Differenz')
    plt.ylabel('Häufigkeit')
    plt.grid(True)
    plt.savefig('media/images/histogram.png')
    plt.close()

def plot_scatter(df):
    # NOTE: Does this graph make sense, and should it use log?
    plt.scatter(df['bValue'].abs(), df['diff'].dropna().abs(), alpha=0.3)
    plt.yscale('log')
    plt.xscale('log')
    plt.title('Scatterplot der Differenzen vs. Werte')
    plt.xlabel('Tatsächlicher Wert (log)')
    plt.ylabel('Differenz (log)')
    plt.grid(True)
    plt.savefig('media/images/scatter.png')
    plt.close()

# Step based analytics

def plot_difference_vs_steps(data):
    big_df = pd.DataFrame()

    all_steps = []
    for entry in data:
        steps = [{'step': 0, 'difference': '0'}]
        steps.extend(
            {'step': index, 'difference': step['difference']}
            for index, step in enumerate(entry['steps'], start=1)
        )
        all_steps.extend(steps)

    big_df = pd.DataFrame(all_steps)
    big_df['step'] = pd.to_numeric(big_df['step'], errors='coerce')
    big_df['difference'] = pd.to_numeric(big_df['difference'], errors='coerce')

    plt.scatter(big_df['step'], big_df['difference'].abs(), color='red', alpha=0.3)

    big_df = big_df.groupby('step')['difference'].mean().reset_index()

    plt.plot(big_df['step'], big_df['difference'], color='blue')
    plt.title('Differenz vs. Schrittzahl')
    plt.xlabel('Schrittzahl')
    plt.ylabel('Differenz')
    plt.grid(True)
    plt.savefig('media/images/difference_vs_steps.png')
    plt.close()

# New Method: Percentage Difference
def plot_percentage_difference(df):
    # Calculate percentage difference
    df['percentage_diff'] = (df['diff'].dropna().abs() / (df['bValue'].dropna().abs())) * 100

    # Plot percentage difference
    # plt.figure(figsize=(10, 6))
    plt.plot(df['percentage_diff'],color='blue')
    plt.title('Percentage Difference Between bValue and dValue')
    plt.xlabel('Index')
    plt.ylabel('Percentage Difference (%)')
    plt.grid(True)
    plt.savefig('media/images/percentage_difference.png')
    plt.close()

def plot_percentage_histogram(df):
    # Calculate percentage difference
    df['percentage_diff'] = (df['diff'] / df['bValue']) * 100

    # Calculate total count of non-NaN values
    total_count = df['percentage_diff'].dropna().count()

    plt.hist(df['percentage_diff'].dropna(), bins=1000)
    plt.title(f'Histogram der Differenzen (Totaleanzahl: {total_count})')
    plt.xlabel('Differenz')
    plt.ylabel('Häufigkeit')
    plt.grid(True)
    plt.savefig('media/images/histogram.png')
    plt.close()

# New Method: Line Plot for Percentage Difference
def plot_percentage_difference_line(df):
    # Calculate percentage difference
    df['percentage_diff'] = (df['diff'].dropna().abs() / (df['bValue'].dropna().abs())) * 100

    # Plot percentage difference as a line plot
    # plt.figure(figsize=(10, 6))
    plt.plot(df.index, df['percentage_diff'], linestyle='-', marker='', color='purple', alpha=0.8, label='Percentage Difference')
    plt.axhline(df['percentage_diff'].mean(), color='red', linestyle='--', label=f'Average: {df["percentage_diff"].mean():.2f}%')
    plt.title('Percentage Difference Between bValue and dValue')
    plt.xlabel('Index')
    plt.ylabel('Percentage Difference (%)')
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig('media/images/percentage_difference_line.png')
    plt.close()


def plot_scatter_with_percentage(df):
    # Calculate percentage difference
    df['percentage_diff'] = (df['diff'].abs() / df['bValue'].abs()) * 100

    # Create scatter plot with log scales
    plt.scatter(df['bValue'].abs(), df['percentage_diff'], alpha=0.3, color='blue')
    plt.xscale('log')  # Log scale for bValue
    plt.title('Scatterplot: bValue vs. Percentage Difference')
    plt.xlabel('bValue (log)')
    plt.ylabel('Percentage Difference (%)')
    plt.grid(True)

    # Save plot
    plt.savefig('media/images/scatter_with_percentage.png')
    plt.close()


if __name__ == "__main__":
    if (len(sys.argv) < 2):
        print("Usage: python analytics.py <json_file>")
        sys.exit(1)

    json_data = load_json(sys.argv[1])
    df = normalize_calculation_data(json_data)

    # plot_histogram(df)
    # plot_all_diferences(df)
    plot_scatter(df)
    # lot_percentage_difference(df)
    # plot_percentage_difference_line(df)
    plot_percentage_histogram(df)
    plot_scatter_with_percentage(df)
    # plot_difference_vs_steps(json_data)
    print("end quit")
