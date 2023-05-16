import openai
import time

def create_openai_config(prompt,
                         engine_name="code-davinci-002",
                         stop=None,
                         max_tokens=3000,
                         top_p=1,
                         temperature=0):
    return {
        "engine": engine_name,
        "prompt": prompt,
        "max_tokens": max_tokens,
        "top_p": top_p,
        "temperature": temperature,
        "logprobs": 1,
        "stop": stop
    }

# Handles requests to OpenAI API
def request_engine(config):
    ret = None
    while ret is None:
        try:
            ret = openai.Completion.create(**config)
        except openai.error.InvalidRequestError as e:
            print(e)
            if "Please reduce your prompt" in str(e):
                config['max_tokens'] = config['max_tokens'] - 200
                if config['max_tokens'] < 100:
                    return None
            else:
                return None
        except openai.error.RateLimitError as e:
            print("Rate limit exceeded. Waiting...")
            time.sleep(60) # wait for a minute
        except openai.error.APIConnectionError as e:
            print("API connection error. Waiting...")
            time.sleep(5)  # wait for a minute
        except:
            print("Unknown error. Waiting...")
            time.sleep(5)  # wait for a minute
    return ret

def create_openai_chat_config(prompt,
                         engine_name="gpt-3.5-turbo"):
    return {
        "model": engine_name,
        "messages": [{"role": "user", "content": prompt}]
    }

# Handles requests to OpenAI API
def request_chat_engine(config):
    ret = None
    while ret is None:
        try:
            ret = openai.ChatCompletion.create(**config)
        except openai.error.InvalidRequestError as e:
            print(e)
            if "Please reduce your prompt" in str(e):
                config['max_tokens'] = config['max_tokens'] - 200
                if config['max_tokens'] < 100:
                    return None
            else:
                return None
        except openai.error.RateLimitError as e:
            print("Rate limit exceeded. Waiting...")
            time.sleep(60) # wait for a minute
        except openai.error.APIConnectionError as e:
            print("API connection error. Waiting...")
            time.sleep(5)  # wait for a minute
        except:
            print("Unknown error. Waiting...")
            time.sleep(5)  # wait for a minute
    return ret