from manim import *
import pandas as pd
import json
import matplotlib.pyplot as plt



# Load the JSON file into a Pandas DataFrame
def load_data(json_file):
    with open(json_file, 'r') as file:
        data = json.load(file)
        print(data)

     # Verify that `data` is a list of dictionaries
    if not isinstance(data, list):
        print(data)
        raise ValueError("JSON data should be a list of dictionaries")

    print("hello")
    # Extract the relevant information into a DataFrame
    results = []
    for entry in data:
        result = entry['result']
        steps = entry['steps']

        # Flatten the data for easier analysis
        for step in steps:
            results.append({
                "doubleResult": float(result['doubleResult']),
                "bigRealResult": float(result['bigRealResult']),
                "step_dValue": float(step['dValue']),
                "step_bdValue": float(step['bdValue']),
                "step_difference": float(step['difference']),
            })

    return pd.DataFrame(results)


# Analyze the differences between doubleResult and bigRealResult
def analyze_differences(df):
    df['overall_difference'] = df['doubleResult'] - df['bigRealResult']
    return df


# Function to create a bar plot and save it as an image
def create_difference_plot(df, output_file='difference_plot.png'):
    plt.figure(figsize=(10, 6))
    df['step_difference'].plot(kind='bar', color='red', alpha=0.7, label='Step Differences')
    plt.title('Difference Between dValue and bdValue at Each Step')
    plt.xlabel('Step Index')
    plt.ylabel('Difference (E-17 scale)')
    plt.tight_layout()
    plt.legend()
    plt.savefig(output_file)
    plt.close()


# Manim scene to display the analysis plot
class DifferencePlotScene(Scene):
    def construct(self):
        # Load the data
        print("Hello")
        json_file = 'calculation_results.json'  # Replace with your JSON file path
        df = load_data(json_file)
        print(json_file)

        # Analyze the differences and create the plot
        df = analyze_differences(df)
        output_image = 'difference_plot.png'
        create_difference_plot(df, output_image)

        # Load the image into a Manim scene
        plot_image = ImageMobject(output_image)
        plot_image.scale(2)  # Scale the image for better visibility

        # Display the image on the screen
        self.play(FadeIn(plot_image))
        self.wait(3)
        self.play(FadeOut(plot_image))

        # Display the calculated overall differences as text
        for index, row in df.iterrows():
            text = Text(
                f"Overall difference for step {index}: {row['overall_difference']:.10f}",
                font_size=24
            )
            self.play(Write(text))
            self.wait(2)
            self.play(FadeOut(text))
