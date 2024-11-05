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
    plt.show()

def plot_histogram(df):
    # NOTE: What is a good bin size?
    plt.hist(df['diff'].dropna(), bins=42)
    plt.title('Histogram der Differenzen')
    plt.xlabel('Differenz')
    plt.ylabel('Häufigkeit')
    plt.grid(True)
    plt.show()

def plot_scatter(df):
    # NOTE: Does this graph make sense, and should it use log?
    plt.scatter(df['bValue'].abs(), df['diff'].dropna().abs(), alpha=0.3)
    plt.yscale('log')
    plt.xscale('log')
    plt.title('Scatterplot der Differenzen vs. Werte')
    plt.xlabel('Tatsächlicher Wert')
    plt.ylabel('Differenz (log)')
    plt.grid(True)
    plt.show()

# Step based analytics

def plot_difference_vs_steps(data):
    big_df = pd.DataFrame()
    for entry in data:
        steps = [{ 'step': 0, 'difference': '0' }]
        for index, step in enumerate(entry['steps'], start=1):
            steps.append(
                {
                    'step': index,
                    'difference': step['difference']
                }
            )
        df = pd.DataFrame(steps)

        df['step'] = pd.to_numeric(df['step'], errors='coerce')
        df['difference'] = pd.to_numeric(df['difference'], errors='coerce')

        big_df = pd.concat([big_df, df])

        plt.scatter(df['step'], df['difference'].abs(), color='red', alpha=0.3)

    big_df = big_df.groupby('step')['difference'].mean().reset_index()

    plt.plot(big_df['step'], big_df['difference'], color='blue')
    plt.title('Differenz vs. Schrittzahl')
    plt.xlabel('Schrittzahl')
    plt.ylabel('Differenz')
    plt.grid(True)
    plt.show()

if __name__ == "__main__":
    if (len(sys.argv) < 2):
        print("Usage: python analytics.py <json_file>")
        sys.exit(1)

    json_data = load_json(sys.argv[1])
    df = normalize_calculation_data(json_data)

    # plot_histogram(df)
    # plot_all_diferences(df)
    # plot_scatter(df)
    
    plot_difference_vs_steps(json_data)
