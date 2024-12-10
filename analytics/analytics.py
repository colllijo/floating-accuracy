import os
import sys
import pandas as pd
import json
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator

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
    df['percentage_diff'] = (df['diff'] / df['bValue']) * 100
    df['percentage_diff_abs'] = df['percentage_diff'].abs()

    return df

# Calculation based analytics
def plot_all_diferences(df):
    plt.plot(df['diff'].dropna())
    plt.title('Alle Differenzen')
    plt.xlabel('Index')
    plt.ylabel('Differenz')
    plt.grid(True)
    plt.savefig(f'media/{data_name}/all_differences.png')
    plt.close()

def plot_histogram(df):
    counts, bins, patches = plt.hist(df['diff'].dropna(), bins=42)
    clip_limit = 100

    # Add an arrow for bins exceeding the limit
    for count, patch in zip(counts, patches):
        if count > clip_limit:
            # Get bin position and width
            x = patch.get_x() + patch.get_width() / 2
            y = clip_limit

            # Add an arrow indicating the excess
            plt.annotate(
                '↑', xy=(x, y), xytext=(x, y - 5),  # Arrow starts just below the limit
                ha='center', va='bottom', color='red', fontsize=14,
                arrowprops=dict(facecolor='red', arrowstyle='wedge')
            )

    plt.title('Histogramm der Differenzen')
    plt.xlabel('Differenz')
    plt.ylabel('Häufigkeit')
    plt.ylim(0, clip_limit)
    plt.grid(True)
    plt.savefig(f'media/{data_name}/histogram.png')
    plt.close()

def plot_scatter(df):
    plt.scatter(df['bValue'].abs(), df['diff'].dropna().abs(), alpha=0.3)
    plt.yscale('log')
    plt.xscale('log')
    plt.title('Streudiagramm der Differenzen vs. Werte')
    plt.xlabel('Erwartete Wert (log)')
    plt.ylabel('Differenz (log)')
    plt.grid(True)
    plt.savefig(f'media/{data_name}/scatter.png')
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

    offset = 1e-70

    big_df = pd.DataFrame(all_steps)
    big_df['step'] = pd.to_numeric(big_df['step'], errors='coerce')
    big_df['difference'] = pd.to_numeric(big_df['difference'], errors='coerce')
    big_df['difference'] = big_df['difference'].abs() + offset

    plt.scatter(big_df['step'], big_df['difference'], color='red', alpha=0.3)

    big_df = big_df.groupby('step')['difference'].mean().reset_index()

    plt.plot(big_df['step'], big_df['difference'], color='blue')
    plt.title('Differenz vs. Schrittzahl')
    plt.xlabel('Schrittzahl')
    plt.ylabel('Differenz (Log)')
    plt.yscale('log')
    plt.grid(True)
    plt.savefig(f'media/{data_name}/difference_vs_steps.png')
    plt.close()

# Percentage based analytics
def plot_percentage_difference_line(df):
    plt.plot(df.index, df['percentage_diff_abs'], label='Prozentsatz Differenz')
    plt.axhline(df['percentage_diff_abs'].dropna().mean(), color='red', linestyle='--', label=f'Durchschnitt: {"{:.2e}".format(df["percentage_diff_abs"].dropna().mean())}%')
    plt.title('Prozentsatz Differenz zwischen BigReal und double Wert')
    plt.xlabel('Index')
    plt.ylabel('Prozentsatz Differenz (%)')
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig(f'media/{data_name}/percentage_difference_line.png')
    plt.close()

def plot_percentage_histogram(df):
    counts, bins, patches = plt.hist(df['percentage_diff'].dropna(), bins=42)
    clip_limit = 100

    # Add an arrow for bins exceeding the limit
    for count, patch in zip(counts, patches):
        if count > clip_limit:
            # Get bin position and width
            x = patch.get_x() + patch.get_width() / 2
            y = clip_limit

            # Add an arrow indicating the excess
            plt.annotate(
                '↑', xy=(x, y), xytext=(x, y - 5),  # Arrow starts just below the limit
                ha='center', va='bottom', color='red', fontsize=14,
                arrowprops=dict(facecolor='red', arrowstyle='wedge')
            )

    plt.title('Histogramm der prozentualen Differenzen')
    plt.xlabel('Differenz (%)')
    plt.ylabel('Häufigkeit')
    plt.ylim(0, clip_limit)
    plt.gca().xaxis.set_major_locator(MaxNLocator(integer=True))
    plt.grid(True)
    plt.savefig(f'media/{data_name}/percentage_histogram.png')
    plt.close()

if __name__ == "__main__":
    if (len(sys.argv) < 2):
        print("Usage: python analytics.py <json_file>")
        sys.exit(1)

    data_name = '-'.join(sys.argv[1].split('-')[:-1])
    json_data = load_json(sys.argv[1])
    df = normalize_calculation_data(json_data)

    os.makedirs(f'media/{data_name}', exist_ok=True)

    plot_histogram(df)
    plot_all_diferences(df)
    plot_scatter(df)
    plot_percentage_difference_line(df)
    plot_percentage_histogram(df)
    plot_difference_vs_steps(json_data)
